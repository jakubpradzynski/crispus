package pl.jakubpradzynski.crispus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.domain.Account;
import pl.jakubpradzynski.crispus.domain.Transaction;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.dto.AccountValuesDto;
import pl.jakubpradzynski.crispus.dto.ChangeAccountNameDto;
import pl.jakubpradzynski.crispus.dto.NewAccountDto;
import pl.jakubpradzynski.crispus.dto.RemoveAccountDto;
import pl.jakubpradzynski.crispus.repositories.AccountRepository;
import pl.jakubpradzynski.crispus.repositories.TransactionRepository;
import pl.jakubpradzynski.crispus.repositories.UserRepository;
import pl.jakubpradzynski.crispus.repositories.UserTypeRepository;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * A service-type class related to accounts.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Returns user used accounts number after asking Account Repository for this data.
     * @param username - user's email
     * @return Integer (user used accounts number)
     */
    public Integer getUserUsedAccountsNumber(String username) {
        User user = userRepository.getUserByEmail(username);
        return accountRepository.getUserUsedAccountsNumber(user);
    }

    public List<Account> getUserAccounts(String username) {
        User user = userRepository.getUserByEmail(username);
        return (List<Account>) accountRepository.getAllUserAccounts(user);
    }

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Finds Account class object asking Account Repository for account by name.
     * Returns transaction expanses amount for previously found account.
     * @param username - user's email
     * @param accountName - name of account
     * @return Double (account expanses amount)
     */
    public Double getUserAccountExpensesAmount(String username, String accountName) {
        User user = userRepository.getUserByEmail(username);
        Account account = accountRepository.getUserAccountByName(user, accountName);
        return transactionRepository.getTransactionExpensesAmountForAccount(account);
    }

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Finds Account class object asking Account Repository for account by name.
     * Returns transaction revenues amount for previously found account.
     * @param username - user's email
     * @param accountName - name of account
     * @return Double (account revenues amount)
     */
    public Double getUserAccountRevenuesAmount(String username, String accountName) {
        User user = userRepository.getUserByEmail(username);
        Account account = accountRepository.getUserAccountByName(user, accountName);
        return transactionRepository.getTransactionRevenuesAmountForAccount(account);
    }

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Finds Account class object asking Account Repository for account by name.
     * Returns amount of money for previously found account.
     * @param username - user's email
     * @param accountName - name of account which money amount we want to receive
     * @return Double (account money amount)
     */
    public Double getUserAccountMoneyAmount(String username, String accountName) {
        User user = userRepository.getUserByEmail(username);
        Account account = accountRepository.getUserAccountByName(user, accountName);
        return account.getMoneyAmount();
    }

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * For each account name create AccountValuesDto with information about: expanses, revenues and money amount.
     * @param username - user's email
     * @param accountNames - names of accounts
     * @return List of AccountValuesDto
     */
    public List<AccountValuesDto> getAccountsValuesDto(String username, List<String> accountNames) {
        User user = userRepository.getUserByEmail(username);
        List<AccountValuesDto> accountValuesDtos = new ArrayList<>();
        accountNames.forEach(accountName -> {
            accountValuesDtos.add(new AccountValuesDto(
                    accountRepository.getUserAccountIdByName(user, accountName),
                    accountName,
                    username,
                    getUserAccountExpensesAmount(username, accountName),
                    getUserAccountRevenuesAmount(username, accountName),
                    getUserAccountMoneyAmount(username, accountName)));
        });
        return accountValuesDtos;
    }

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Returns account number available for user after asking User Type Repository for this data.
     * @param username - user's email
     * @return Integer (user available account number)
     */
    public Integer getUserAvailableAccountNumber(String username) {
        User user = userRepository.getUserByEmail(username);
        return userTypeRepository.getAccountNumberAvailableForUser(user);
    }

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Changes user's account name by calling function from Account Repository.
     * @param username - user's email
     * @param changeAccountNameDto - data for changing account name
     */
    @Transactional
    public void changeUserAccountName(String username, ChangeAccountNameDto changeAccountNameDto) {
        User user = userRepository.getUserByEmail(username);
        accountRepository.changeUserAccountByName(user, changeAccountNameDto);
    }

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Creates new user account by calling function from Account Repository.
     * @param newAccountDto - data about new account
     */
    @Transactional
    public void addNewUserAccount(NewAccountDto newAccountDto) {
        User user = userRepository.getUserByEmail(newAccountDto.getUsername());
        accountRepository.createAccount(user, newAccountDto.getName(), newAccountDto.getMoneyAmount());
    }

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Removes user's account with all transactions assigned to this account.
     * @param removeAccountDto - data about removing account
     */
    @Transactional
    public void removeUserAccount(RemoveAccountDto removeAccountDto) {
        User user = userRepository.getUserByEmail(removeAccountDto.getUsername());
        Account account = accountRepository.getUserAccountByName(user, removeAccountDto.getName());
        List<Transaction> accountTransactions = (List<Transaction>) transactionRepository.getAllTransactionsAssignedToAccount(account);
        accountTransactions.forEach(transaction -> transactionRepository.removeTransaction(transaction.getId()));;
        accountRepository.removeAccount(account.getId());
    }
}
