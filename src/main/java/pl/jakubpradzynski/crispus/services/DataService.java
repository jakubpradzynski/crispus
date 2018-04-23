package pl.jakubpradzynski.crispus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.dto.PublicUserData;
import pl.jakubpradzynski.crispus.dto.TransactionDto;
import pl.jakubpradzynski.crispus.domain.Transaction;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.repositories.*;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PlaceRepository placeRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionCategoryRepository transactionCategoryRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    AccountService accountService;

    public PublicUserData getPublicUserData(String username) {
        User user = userRepository.getUserByEmail(username);
        Integer usedAccounts = accountService.getUserUsedAccountsNumber(username);
        Integer usedPlaces = placeRepository.getUserUsedPlacesNumber(user);
        Integer usedTransactionCategories = transactionCategoryRepository.getUserUsedTransactionCategoriesNumber(user);
        PublicUserData publicUserData = new PublicUserData(user.getName(), user.getSurname(), user.getEmail(), user.getPhoneNumber(), user.getUserType(), usedAccounts, usedPlaces, usedTransactionCategories);
        return publicUserData;
    }

    public List<TransactionDto> getUserLastTenTransactionsDto(String username) {
        User user = userRepository.getUserByEmail(username);
        List<TransactionDto> userLastTenTransactionsDto = (List<TransactionDto>) transactionRepository.getLastTenUserTransactionsDto(user);
        return userLastTenTransactionsDto;
    }

    public List<String> getUserAccountsNames(String username) {
        User user = userRepository.getUserByEmail(username);
        List<String> userAccountsNames = (List<String>) accountRepository.getAllUserAccountsNames(user);
        return userAccountsNames;
    }

    public List<String> getPlacesDescriptionsAvailableForUser(String username) {
        User user = userRepository.getUserByEmail(username);
        List<String> placesDescriptions = (List<String>) placeRepository.getAllPlacesDescriptionsAvailableForUser(user);
        return placesDescriptions;
    }

    public List<String> getTransactionCategoriesNamesAvailableForUser(String username) {
        User user = userRepository.getUserByEmail(username);
        List<String> transactionCategoryNames = (List<String>) transactionCategoryRepository.getAllTransactionCategoriesNamesAvailableForUser(user);
        return transactionCategoryNames;
    }
}
