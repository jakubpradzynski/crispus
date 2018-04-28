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

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    UserTypeRepository userTypeRepository;

    public Integer getUserUsedAccountsNumber(String username) {
        User user = userRepository.getUserByEmail(username);
        return accountRepository.getUserUsedAccountsNumber(user);
    }

    public List<Account> getUserAccounts(String username) {
        User user = userRepository.getUserByEmail(username);
        return (List<Account>) accountRepository.getAllUserAccounts(user);
    }

    public Double getUserAccountExpensesAmount(String username, String accountName) {
        User user = userRepository.getUserByEmail(username);
        Account account = accountRepository.getUserAccountByName(user, accountName);
        return transactionRepository.getTransactionExpensesAmountForAccount(account);
    }

    public Double getUserAccountRevenuesAmount(String username, String accountName) {
        User user = userRepository.getUserByEmail(username);
        Account account = accountRepository.getUserAccountByName(user, accountName);
        return transactionRepository.getTransactionRevenuesAmountForAccount(account);
    }

    public Double getUserAccountMoneyAmount(String username, String accountName) {
        User user = userRepository.getUserByEmail(username);
        Account account = accountRepository.getUserAccountByName(user, accountName);
        return account.getMoneyAmount();
    }

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

    public Integer getUserAvailableAccountNumber(String username) {
        User user = userRepository.getUserByEmail(username);
        return userTypeRepository.getAccountNumberAvailableForUser(user);
    }

    @Transactional
    public void changeUserAccountName(String username, ChangeAccountNameDto changeAccountNameDto) {
        User user = userRepository.getUserByEmail(username);
        accountRepository.changeUserAccountByName(user, changeAccountNameDto);
    }

    @Transactional
    public void addNewUserAccount(NewAccountDto newAccountDto) {
        User user = userRepository.getUserByEmail(newAccountDto.getUsername());
        accountRepository.createAccount(user, newAccountDto.getName(), newAccountDto.getMoneyAmount());
    }

    @Transactional
    public void removeUserAccount(RemoveAccountDto removeAccountDto) {
        User user = userRepository.getUserByEmail(removeAccountDto.getUsername());
        System.out.println(user);
        Account account = accountRepository.getUserAccountByName(user, removeAccountDto.getName());
        System.out.println(account);
        List<Transaction> accountTransactions = (List<Transaction>) transactionRepository.getAllTransactionsAssignedToAccount(account);
        System.out.println(accountTransactions);
        accountTransactions.forEach(transaction -> transactionRepository.removeTransaction(transaction.getId()));
        System.out.println(accountTransactions);
        accountRepository.removeAccount(account.getId());
    }
}
