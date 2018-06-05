package pl.jakubpradzynski.crispus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.domain.Category;
import pl.jakubpradzynski.crispus.domain.Transaction;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.dto.CategoryDto;
import pl.jakubpradzynski.crispus.dto.TransactionDto;
import pl.jakubpradzynski.crispus.exceptions.CategoryExistsException;
import pl.jakubpradzynski.crispus.repositories.CategoryRepository;
import pl.jakubpradzynski.crispus.repositories.TransactionRepository;
import pl.jakubpradzynski.crispus.repositories.UserRepository;
import pl.jakubpradzynski.crispus.repositories.UserTypeRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
public class CategoryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Integer getUserUsedCategoriesNumber(String username) {
        User user = userRepository.getUserByEmail(username);
        return categoryRepository.getUserUsedCategoriesNumber(user);
    }

    public Integer getUserAvailableCategoriesNumber(String username) {
        User user = userRepository.getUserByEmail(username);
        return userTypeRepository.getCategoryNumberAvailableForUser(user);
    }

    public List<CategoryDto> getPreDefinedCategories() {
        Collection<Category> allPreDefinedCategories = categoryRepository.getAllPreDefinedCategories();
        List<CategoryDto> preDefinedCategoriessDto = new ArrayList<>();
        if (allPreDefinedCategories.size() > 0) allPreDefinedCategories.forEach(transactionCategory -> preDefinedCategoriessDto.add(CategoryDto.fromTransactionCategory(transactionCategory)));
        return preDefinedCategoriessDto;
    }

    public List<CategoryDto> getUserCreatedCategories(String username) {
        User user = userRepository.getUserByEmail(username);
        Collection<Category> userCreatedCategories = categoryRepository.getCategoriesCreatedByUser(user);
        List<CategoryDto> userCreateCategoriessDto = new ArrayList<>();
        userCreatedCategories.forEach(transactionCategory -> userCreateCategoriessDto.add(CategoryDto.fromTransactionCategory(transactionCategory)));
        return userCreateCategoriessDto;
    }

    @Transactional
    public void addNewUserCategory(String username, CategoryDto categoryDto) throws CategoryExistsException {
        User user = userRepository.getUserByEmail(username);
        Category category = categoryRepository.getCategoryByName(categoryDto.getName());
        if (category != null) {
            if (category.getUsers().isEmpty()) {
                throw new CategoryExistsException("Użytkownik nie może tworzyć kategorii, która jest predefiniowana");
            }
            if (category.getUsers().contains(user)) {
                throw new CategoryExistsException("Użytkownik już stworzył taką kategorię");
            }
            category.getUsers().add(user);
            categoryRepository.updateCategory(category);
        } else {
            Set<User> users = new HashSet<>(Arrays.asList(user));
            categoryRepository.createTransactionCategory(categoryDto.getName(), users, 'F');
        }
    }

    public CategoryDto getTransactionCategoryDtoById(Integer id) {
        return CategoryDto.fromTransactionCategory(categoryRepository.getCategoryById(id));
    }

    public List<TransactionDto> getUserCategoryTransactionsDtoByIdInRange(String username, Integer id, Integer start, Integer max) {
        User user = userRepository.getUserByEmail(username);
        Category category = categoryRepository.getCategoryById(id);
        List<Transaction> transactions = (List<Transaction>) transactionRepository.getUserTransactionsAssignedToCategoryInRange(user, category, start, max);
        List<TransactionDto> transactionDtos = new ArrayList<>();
        if (!transactions.isEmpty()) transactions.forEach(transaction -> transactionDtos.add(TransactionDto.fromTransaction(transaction)));
        return transactionDtos;
    }

    @Transactional
    public void removeUserCategory(String username, Integer id) {
        User user = userRepository.getUserByEmail(username);
        Category category = categoryRepository.getCategoryById(id);
        transactionRepository.removeCategoryFromTransacitons(user, category);
        if (category.getUsers().size() == 1) {
            categoryRepository.removeCategory(id);
        } else {
            category.getUsers().remove(user);
            categoryRepository.updateCategory(category);
        }
    }

    @Transactional
    public Integer changeUserCategoryName(String username, CategoryDto categoryDto) throws CategoryExistsException {
        User user = userRepository.getUserByEmail(username);
        Category oldCategory = categoryRepository.getCategoryById(categoryDto.getId());
        Category newCategory = categoryRepository.getCategoryByName(categoryDto.getName());
        if (oldCategory.getUsers().size() == 1) {
            if (newCategory == null) {
                categoryRepository.changeUserCategoryName(user, categoryDto);
                return -1;
            } else if (newCategory.getUsers().contains(user)) {
                throw new CategoryExistsException("Nie możesz zmienić nazwę miejsca na nazwę już istniejącego!");
            } else {
                newCategory.getUsers().add(user);
                categoryRepository.updateCategory(newCategory);
                transactionRepository.changeCategoryInUserTransactions(user, oldCategory, newCategory);
                categoryRepository.removeCategory(oldCategory.getId());
                return newCategory.getId();
            }
        } else {
            if (newCategory == null) {
                categoryRepository.createTransactionCategory(categoryDto.getName(), new HashSet<>(Arrays.asList(user)), 'F');
                Category createdCategory = categoryRepository.getCategoryByName(categoryDto.getName());
                transactionRepository.changeCategoryInUserTransactions(user, oldCategory, createdCategory);
                oldCategory.getUsers().remove(user);
                categoryRepository.updateCategory(oldCategory);
                return createdCategory.getId();
            } else if (newCategory.getUsers().contains(user)) {
                throw new CategoryExistsException("Nie możesz zmienić nazwę miejsca na nazwę już istniejącego!");
            } else {
                newCategory.getUsers().add(user);
                categoryRepository.updateCategory(newCategory);
                transactionRepository.changeCategoryInUserTransactions(user, oldCategory, newCategory);
                oldCategory.getUsers().remove(user);
                categoryRepository.updateCategory(oldCategory);
                return newCategory.getId();
            }
        }
    }
}
