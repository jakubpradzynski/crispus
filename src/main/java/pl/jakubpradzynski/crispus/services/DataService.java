package pl.jakubpradzynski.crispus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.dto.PublicUserData;
import pl.jakubpradzynski.crispus.dto.TransactionDto;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.repositories.*;

import java.util.List;

/**
 * A service-type class related to general data.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Service
public class DataService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    /**
     * Method finds User class object asking User RepositoryClass for user by specific email.
     * Returns data about user such as used number of accounts, places and categories, and limits from user type.
     * @param username - user's email
     * @return PublicUserData
     */
    public PublicUserData getPublicUserData(String username) {
        User user = userRepository.getUserByEmail(username);
        Integer usedAccounts = accountService.getUserUsedAccountsNumber(username);
        Integer usedPlaces = placeRepository.getUserUsedPlacesNumber(user);
        Integer usedTransactionCategories = categoryRepository.getUserUsedCategoriesNumber(user);
        PublicUserData publicUserData = new PublicUserData(user.getName(), user.getSurname(), user.getEmail(), user.getPhoneNumber(), user.getUserType(), usedAccounts, usedPlaces, usedTransactionCategories);
        return publicUserData;
    }

    /**
     * Method finds User class object asking User RepositoryClass for user by specific email.
     * Returns last ten transactions info after call a function from Transaction RepositoryClass.
     * @param username - user's email
     * @return List of TransactionDto
     */
    public List<TransactionDto> getUserLastTenTransactionsDto(String username) {
        User user = userRepository.getUserByEmail(username);
        List<TransactionDto> userLastTenTransactionsDto = (List<TransactionDto>) transactionRepository.getLastTenUserTransactionsDto(user);
        return userLastTenTransactionsDto;
    }

    /**
     * Method finds User class object asking User RepositoryClass for user by specific email.
     * Returns accounts names available for user after call a function from Account RepositoryClass.
     * @param username - user's email
     * @return List of String (accounts names)
     */
    public List<String> getUserAccountsNames(String username) {
        User user = userRepository.getUserByEmail(username);
        List<String> userAccountsNames = (List<String>) accountRepository.getAllUserAccountsNames(user);
        return userAccountsNames;
    }

    /**
     * Method finds User class object asking User RepositoryClass for user by specific email.
     * Returns places names available for user after call a function from Place RepositoryClass.
     * @param username - user's email
     * @return List of String (places names)
     */
    public List<String> getPlacesNamesAvailableForUser(String username) {
        User user = userRepository.getUserByEmail(username);
        List<String> placesNames = (List<String>) placeRepository.getAllPlacesNamesAvailableForUser(user);
        return placesNames;
    }

    /**
     * Method finds User class object asking User RepositoryClass for user by specific email.
     * Returns categories names available for user after call a function from Category RepositoryClass.
     * @param username - user's email
     * @return List of String (categories names)
     */
    public List<String> getTransactionCategoriesNamesAvailableForUser(String username) {
        User user = userRepository.getUserByEmail(username);
        List<String> transactionCategoryNames = (List<String>) categoryRepository.getAllCategoriesNamesAvailableForUser(user);
        return transactionCategoryNames;
    }
}
