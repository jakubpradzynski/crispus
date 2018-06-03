package pl.jakubpradzynski.crispus.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.jakubpradzynski.crispus.dto.MonthlyBudgetInfoDto;
import pl.jakubpradzynski.crispus.repositories.MonthlyBudgetRepository;
import pl.jakubpradzynski.crispus.repositories.TransactionRepository;
import pl.jakubpradzynski.crispus.repositories.UserRepository;
import pl.jakubpradzynski.crispus.utils.DateUtils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
public class MonthlyBudgetServiceTest {

    @TestConfiguration
    static class MonthlyBudgetServiceTestContextConfiguration {

        @Bean
        public MonthlyBudgetService monthlyBudgetService() {
            return new MonthlyBudgetService();
        }
    }

    @Autowired
    private MonthlyBudgetService monthlyBudgetService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MonthlyBudgetRepository monthlyBudgetRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @Test
    public void sortMonthlyBudgetInfoDtosTest() throws ParseException {
        List<MonthlyBudgetInfoDto> monthlyBudgetInfoDtoList = Arrays.asList(
                new MonthlyBudgetInfoDto(1, "username", DateUtils.stringToDate("01-05-2018", "dd-MM-yyyy"), DateUtils.stringToDate("31-05-2018", "dd-MM-yyyy"), 13.),
                new MonthlyBudgetInfoDto(2, "username", DateUtils.stringToDate("01-06-2018", "dd-MM-yyyy"), DateUtils.stringToDate("30-06-2018", "dd-MM-yyyy"), 14.),
                new MonthlyBudgetInfoDto(3, "username", DateUtils.stringToDate("30-04-2018", "dd-MM-yyyy"), DateUtils.stringToDate("30-04-2018", "dd-MM-yyyy"), 15.));
        List<MonthlyBudgetInfoDto> sortedList = monthlyBudgetService.sortMonthlyBudgetInfoDtos(monthlyBudgetInfoDtoList);
        Assert.assertTrue(sortedList.get(0).getId() == 2 && sortedList.get(2).getId() == 3);
    }
}
