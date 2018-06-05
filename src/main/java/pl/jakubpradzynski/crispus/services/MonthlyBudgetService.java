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

@Service
public class MonthlyBudgetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MonthlyBudgetRepository monthlyBudgetRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public MonthlyBudgetInfoDto getUserActualMonthlyBudgetDto(String username) {
        User user = userRepository.getUserByEmail(username);
        MonthlyBudgetInfoDto actualBudget = monthlyBudgetRepository.getUserActualMonthlyBudgetDto(user);
        if (actualBudget == null) return actualBudget;
        actualBudget.setUsedAmount(transactionRepository.getUserBudgetUsedAmount(user, actualBudget));
        actualBudget.setDifference(actualBudget.getAmount() + actualBudget.getUsedAmount());
        return actualBudget;
    }

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

    protected List<MonthlyBudgetInfoDto> sortMonthlyBudgetInfoDtos(List<MonthlyBudgetInfoDto> monthlyBudgetInfoDtoList) {
        monthlyBudgetInfoDtoList.sort(Comparator.comparing(o -> o.getStartDate()));
        Collections.reverse(monthlyBudgetInfoDtoList);
        return monthlyBudgetInfoDtoList;
    }

    @Transactional
    public void addNewUserMonthlyBudget(String username, NewMonthlyBudgetDto newMonthlyBudgetDto) throws ParseException {
        User user = userRepository.getUserByEmail(username);
        monthlyBudgetRepository.createMonthlyBudget(user, DateUtils.stringToDate(newMonthlyBudgetDto.getStartDate(), "yyyy-MM-dd"),DateUtils.stringToDate(newMonthlyBudgetDto.getEndDate(), "yyyy-MM-dd"), newMonthlyBudgetDto.getAmount());
    }

    public MonthlyBudgetInfoDto getBudgetInfoById(Integer id) {
        MonthlyBudgetInfoDto monthlyBudgetInfoDto = MonthlyBudgetInfoDto.fromMonthlyBudget(monthlyBudgetRepository.getMonthlyBudgetById(id));
        monthlyBudgetInfoDto.setUsedAmount(transactionRepository.getUserBudgetUsedAmount(userRepository.getUserByEmail(monthlyBudgetInfoDto.getUsername()), monthlyBudgetInfoDto));
        monthlyBudgetInfoDto.setDifference(monthlyBudgetInfoDto.getAmount() + monthlyBudgetInfoDto.getUsedAmount());
        return monthlyBudgetInfoDto;
    }

    public List<TransactionDto> getUserBudgetTransactionsList(String username, MonthlyBudgetInfoDto monthlyBudgetInfoDto) {
        User user = userRepository.getUserByEmail(username);
        List<Transaction> transactions = (List<Transaction>) transactionRepository.getUserTransactionsInBudget(user, monthlyBudgetInfoDto);
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        transactions.forEach(transaction -> transactionDtoList.add(TransactionDto.fromTransaction(transaction)));
        return transactionDtoList;
    }
}
