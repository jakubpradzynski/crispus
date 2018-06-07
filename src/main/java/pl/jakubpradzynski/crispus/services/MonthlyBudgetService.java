package pl.jakubpradzynski.crispus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.domain.MonthlyBudget;
import pl.jakubpradzynski.crispus.domain.Transaction;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.dto.MonthlyBudgetInfoDto;
import pl.jakubpradzynski.crispus.dto.NewMonthlyBudgetDto;
import pl.jakubpradzynski.crispus.dto.TransactionDto;
import pl.jakubpradzynski.crispus.repositories.MonthlyBudgetRepository;
import pl.jakubpradzynski.crispus.repositories.TransactionRepository;
import pl.jakubpradzynski.crispus.repositories.UserRepository;
import pl.jakubpradzynski.crispus.utils.DateUtils;

import org.springframework.transaction.annotation.Transactional;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A service-type class related to monthly budgets.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Service
public class MonthlyBudgetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MonthlyBudgetRepository monthlyBudgetRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Method finds User class object asking User RepositoryClass for user by specific email.
     * Returns info about actual monthly budget if exists or null after receive data from Monthly Budget RepositoryClass and Transaction RepositoryClass.
     * @param username - user's email
     * @return MonthlyBudgetInfoDto
     */
    public MonthlyBudgetInfoDto getUserActualMonthlyBudgetDto(String username) {
        User user = userRepository.getUserByEmail(username);
        MonthlyBudgetInfoDto actualBudget = monthlyBudgetRepository.getUserActualMonthlyBudgetDto(user);
        if (actualBudget == null) return actualBudget;
        actualBudget.setUsedAmount(transactionRepository.getUserBudgetUsedAmount(user, actualBudget));
        actualBudget.setDifference(actualBudget.getAmount() + actualBudget.getUsedAmount());
        return actualBudget;
    }

    /**
     * Method finds User class object asking User RepositoryClass for user by specific email.
     * Returns info about last user's monthly budgets after receive data from Monthly Budget RepositoryClass and Transaction RepositoryClass.
     * @param username - user's email
     * @return List of MonthlyBudgetInfoDto
     */
    public List<MonthlyBudgetInfoDto> getUserLastMonthlyBudgetsDto(String username) {
        User user = userRepository.getUserByEmail(username);
        List<MonthlyBudget> monthlyBudgetList = (List<MonthlyBudget>) monthlyBudgetRepository.getAllUserMonthlyBudgets(user);
        List<MonthlyBudgetInfoDto> monthlyBudgetInfoDtoList = new ArrayList<>();
        monthlyBudgetList.forEach(monthlyBudget -> monthlyBudgetInfoDtoList.add(MonthlyBudgetInfoDto.fromMonthlyBudget(monthlyBudget)));
        monthlyBudgetInfoDtoList.forEach(monthlyBudgetInfoDto -> {
            monthlyBudgetInfoDto.setUsedAmount(transactionRepository.getUserBudgetUsedAmount(user, monthlyBudgetInfoDto));
            monthlyBudgetInfoDto.setDifference(monthlyBudgetInfoDto.getAmount() + monthlyBudgetInfoDto.getUsedAmount());
        });
        return sortMonthlyBudgetInfoDtos(monthlyBudgetInfoDtoList);
    }

    /**
     * Method finds User class object asking User RepositoryClass for user by specific email.
     * Returns sorted list of info about monthly budgets.
     * Sort descending by date.
     * @param monthlyBudgetInfoDtoList - list of bugets info to sort
     * @return List of MonthlyBudgetInfoDto (sorted)
     */
    protected List<MonthlyBudgetInfoDto> sortMonthlyBudgetInfoDtos(List<MonthlyBudgetInfoDto> monthlyBudgetInfoDtoList) {
        monthlyBudgetInfoDtoList.sort(Comparator.comparing(o -> o.getStartDate()));
        Collections.reverse(monthlyBudgetInfoDtoList);
        return monthlyBudgetInfoDtoList;
    }

    /**
     * Method finds User class object asking User RepositoryClass for user by specific email.
     * Calls a function from Monthly Budget RepositoryClass to create new user's monthly budget.
     * @param username - user's email
     * @param newMonthlyBudgetDto - data about new user's monthly budget
     * @throws ParseException  - Exception is thrown when it is impossible to parse the date from the string.
     */
    @Transactional
    public void addNewUserMonthlyBudget(String username, NewMonthlyBudgetDto newMonthlyBudgetDto) throws ParseException {
        User user = userRepository.getUserByEmail(username);
        monthlyBudgetRepository.createMonthlyBudget(user, DateUtils.stringToDate(newMonthlyBudgetDto.getStartDate(), "yyyy-MM-dd"),DateUtils.stringToDate(newMonthlyBudgetDto.getEndDate(), "yyyy-MM-dd"), newMonthlyBudgetDto.getAmount());
    }

    /**
     * Method finds User class object asking User RepositoryClass for user by specific email.
     * Returns info about budget specific by id after receive data from Monthly Budget RepositoryClass and Transaction RepositoryClass.
     * @param id - budget id
     * @return MonthlyBudgetInfoDto
     */
    public MonthlyBudgetInfoDto getBudgetInfoById(Integer id) {
        MonthlyBudgetInfoDto monthlyBudgetInfoDto = MonthlyBudgetInfoDto.fromMonthlyBudget(monthlyBudgetRepository.getById(id));
        monthlyBudgetInfoDto.setUsedAmount(transactionRepository.getUserBudgetUsedAmount(userRepository.getUserByEmail(monthlyBudgetInfoDto.getUsername()), monthlyBudgetInfoDto));
        monthlyBudgetInfoDto.setDifference(monthlyBudgetInfoDto.getAmount() + monthlyBudgetInfoDto.getUsedAmount());
        return monthlyBudgetInfoDto;
    }

    /**
     * Method finds User class object asking User RepositoryClass for user by specific email.
     * Returns list of transactions info in the budget after calling Transaction RepositoryClass for this data.
     * @param username - user's email
     * @param monthlyBudgetInfoDto - info about budget which transactions we want to receive
     * @return List of TransactionDto
     */
    public List<TransactionDto> getUserBudgetTransactionsList(String username, MonthlyBudgetInfoDto monthlyBudgetInfoDto) {
        User user = userRepository.getUserByEmail(username);
        List<Transaction> transactions = (List<Transaction>) transactionRepository.getUserTransactionsInBudget(user, monthlyBudgetInfoDto);
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        transactions.forEach(transaction -> transactionDtoList.add(TransactionDto.fromTransaction(transaction)));
        return transactionDtoList;
    }
}
