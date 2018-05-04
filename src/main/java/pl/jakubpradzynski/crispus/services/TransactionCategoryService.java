package pl.jakubpradzynski.crispus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.domain.Transaction;
import pl.jakubpradzynski.crispus.domain.TransactionCategory;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.dto.TransactionCategoryDto;
import pl.jakubpradzynski.crispus.dto.TransactionDto;
import pl.jakubpradzynski.crispus.exceptions.TransactionCategoryExistsException;
import pl.jakubpradzynski.crispus.repositories.TransactionCategoryRepository;
import pl.jakubpradzynski.crispus.repositories.TransactionRepository;
import pl.jakubpradzynski.crispus.repositories.UserRepository;
import pl.jakubpradzynski.crispus.repositories.UserTypeRepository;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class TransactionCategoryService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionCategoryRepository transactionCategoryRepository;

    @Autowired
    UserTypeRepository userTypeRepository;

    @Autowired
    TransactionRepository transactionRepository;

    public Integer getUserUsedCategoriesNumber(String username) {
        User user = userRepository.getUserByEmail(username);
        return transactionCategoryRepository.getUserUsedTransactionCategoriesNumber(user);
    }

    public Integer getUserAvailableCategoriesNumber(String username) {
        User user = userRepository.getUserByEmail(username);
        return userTypeRepository.getCategoryNumberAvailableForUser(user);
    }

    public List<TransactionCategoryDto> getPreDefinedCategories() {
        Collection<TransactionCategory> allPreDefinedCategories = transactionCategoryRepository.getAllPreDefinedTransactionCategories();
        List<TransactionCategoryDto> preDefinedCategoriessDto = new ArrayList<>();
        if (allPreDefinedCategories.size() > 0) allPreDefinedCategories.forEach(transactionCategory -> preDefinedCategoriessDto.add(TransactionCategoryDto.fromTransactionCategory(transactionCategory)));
        return preDefinedCategoriessDto;
    }

    public List<TransactionCategoryDto> getUserCreatedCategories(String username) {
        User user = userRepository.getUserByEmail(username);
        Collection<TransactionCategory> userCreatedCategories = transactionCategoryRepository.getCategoriesCreatedByUser(user);
        List<TransactionCategoryDto> userCreateCategoriessDto = new ArrayList<>();
        userCreatedCategories.forEach(transactionCategory -> userCreateCategoriessDto.add(TransactionCategoryDto.fromTransactionCategory(transactionCategory)));
        return userCreateCategoriessDto;
    }

    @Transactional
    public void addNewUserCategory(String username, TransactionCategoryDto transactionCategoryDto) throws TransactionCategoryExistsException {
        User user = userRepository.getUserByEmail(username);
        TransactionCategory transactionCategory = transactionCategoryRepository.getTransactionCategoryByName(transactionCategoryDto.getName());
        if (transactionCategory != null) {
            if (transactionCategory.getUsers().isEmpty()) {
                throw new TransactionCategoryExistsException("Użytkownik nie może tworzyć kategorii, która jest predefiniowana");
            }
            if (transactionCategory.getUsers().contains(user)) {
                throw new TransactionCategoryExistsException("Użytkownik już stworzył taką kategorię");
            }
            transactionCategory.getUsers().add(user);
            transactionCategoryRepository.updateTransactionCategory(transactionCategory);
        } else {
            Set<User> users = new HashSet<>(Arrays.asList(user));
            transactionCategoryRepository.createTransactionCategory(transactionCategoryDto.getName(), users);
        }
    }

    public TransactionCategoryDto getTransactionCategoryDtoById(Integer id) {
        return TransactionCategoryDto.fromTransactionCategory(transactionCategoryRepository.getTransactionCategoryById(id));
    }

    public List<TransactionDto> getUserCategoryTransactionsDtoByIdInRange(String username, Integer id, Integer start, Integer max) {
        User user = userRepository.getUserByEmail(username);
        TransactionCategory transactionCategory = transactionCategoryRepository.getTransactionCategoryById(id);
        List<Transaction> transactions = (List<Transaction>) transactionRepository.getUserTransactionsAssignedToCategoryInRange(user, transactionCategory, start, max);
        List<TransactionDto> transactionDtos = new ArrayList<>();
        if (!transactions.isEmpty()) transactions.forEach(transaction -> transactionDtos.add(TransactionDto.fromTransaction(transaction)));
        return transactionDtos;
    }

    @Transactional
    public void removeUserCategory(String username, Integer id) {
        User user = userRepository.getUserByEmail(username);
        TransactionCategory transactionCategory = transactionCategoryRepository.getTransactionCategoryById(id);
        transactionRepository.removeCategoryFromTransacitons(user, transactionCategory);
        if (transactionCategory.getUsers().size() == 1) {
            transactionCategoryRepository.removeTransactionCategory(id);
        } else {
            transactionCategory.getUsers().remove(user);
            transactionCategoryRepository.updateTransactionCategory(transactionCategory);
        }
    }

    @Transactional
    public Integer changeUserCategoryName(String username, TransactionCategoryDto transactionCategoryDto) throws TransactionCategoryExistsException {
        User user = userRepository.getUserByEmail(username);
        TransactionCategory oldCategory = transactionCategoryRepository.getTransactionCategoryById(transactionCategoryDto.getId());
        TransactionCategory newCategory = transactionCategoryRepository.getTransactionCategoryByName(transactionCategoryDto.getName());
        if (oldCategory.getUsers().size() == 1) {
            if (newCategory == null) {
                transactionCategoryRepository.changeUserCategoryName(user, transactionCategoryDto);
                return -1;
            } else if (newCategory.getUsers().contains(user)) {
                throw new TransactionCategoryExistsException("Nie możesz zmienić nazwę miejsca na nazwę już istniejącego!");
            } else {
                newCategory.getUsers().add(user);
                transactionCategoryRepository.updateTransactionCategory(newCategory);
                transactionRepository.changeCategoryInUserTransactions(user, oldCategory, newCategory);
                transactionCategoryRepository.removeTransactionCategory(oldCategory.getId());
                return newCategory.getId();
            }
        } else {
            if (newCategory == null) {
                transactionCategoryRepository.createTransactionCategory(transactionCategoryDto.getName(), new HashSet<>(Arrays.asList(user)));
                TransactionCategory createdCategory = transactionCategoryRepository.getTransactionCategoryByName(transactionCategoryDto.getName());
                transactionRepository.changeCategoryInUserTransactions(user, oldCategory, createdCategory);
                oldCategory.getUsers().remove(user);
                transactionCategoryRepository.updateTransactionCategory(oldCategory);
                return createdCategory.getId();
            } else if (newCategory.getUsers().contains(user)) {
                throw new TransactionCategoryExistsException("Nie możesz zmienić nazwę miejsca na nazwę już istniejącego!");
            } else {
                newCategory.getUsers().add(user);
                transactionCategoryRepository.updateTransactionCategory(newCategory);
                transactionRepository.changeCategoryInUserTransactions(user, oldCategory, newCategory);
                oldCategory.getUsers().remove(user);
                transactionCategoryRepository.updateTransactionCategory(oldCategory);
                return newCategory.getId();
            }
        }
    }
}
