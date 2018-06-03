package pl.jakubpradzynski.crispus.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.dto.*;
import pl.jakubpradzynski.crispus.exceptions.EmailExistsException;
import pl.jakubpradzynski.crispus.exceptions.PlaceExistsException;
import pl.jakubpradzynski.crispus.exceptions.TransactionCategoryExistsException;
import pl.jakubpradzynski.crispus.repositories.*;
import pl.jakubpradzynski.crispus.services.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

@Component
@Scope("singleton")
public class Starter implements CommandLineRunner {

    @Override
    public void run(String... args) {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.forLanguageTag("pl"));
        if (userRepository.getUserByEmail("jankowalski@gmail.com") == null) {
            Runnable runnable = () -> initDB();
            Thread initThread = new Thread(runnable,"Wątek initializujący dane w bazie danych");
            initThread.start();
        }
    }

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserTypeService userTypeService;

    @Autowired
    private PlaceService placeService;

    @Autowired
    private MonthlyBudgetService monthlyBudgetService;

    private void initDB() {
        userTypeRepository.createUserType("normal", 2, 5, 5);
        userTypeRepository.createUserType("premium", 4, 15, 15);
        placeRepository.createPlace("Biedronka", null, 'T');
        placeRepository.createPlace("Lidl", null, 'T');
        placeRepository.createPlace("Tesco", null, 'T');
        placeRepository.createPlace("Auchan", null, 'T');
        placeRepository.createPlace("Polo Market", null, 'T');
        placeRepository.createPlace("Żabka", null, 'T');
        placeRepository.createPlace("Piotr i Paweł", null, 'T');
        placeRepository.createPlace("Dino", null, 'T');
        placeRepository.createPlace("Stokrotka", null, 'T');
        placeRepository.createPlace("Intermarche", null, 'T');
        placeRepository.createPlace("PKO BP", null, 'T');
        placeRepository.createPlace("ING Bank Śląski", null, 'T');
        placeRepository.createPlace("mBank", null, 'T');
        placeRepository.createPlace("Bank Zachodni WBK", null, 'T');
        placeRepository.createPlace("Bank Pekao", null, 'T');
        placeRepository.createPlace("Alior Bank", null, 'T');
        placeRepository.createPlace("Bank Millennium", null, 'T');
        placeRepository.createPlace("Bank Citi Handlowy", null, 'T');
        placeRepository.createPlace("Getin Noble Bank", null, 'T');
        placeRepository.createPlace("Raiffeisen Polbank", null, 'T');
        categoryRepository.createTransactionCategory("Żywność", null, 'T');
        categoryRepository.createTransactionCategory("Jedzenie na mieście", null, 'T');
        categoryRepository.createTransactionCategory("Napoje bezalkoholowe", null, 'T');
        categoryRepository.createTransactionCategory("Papierosy", null, 'T');
        categoryRepository.createTransactionCategory("Alkohole", null, 'T');
        categoryRepository.createTransactionCategory("Rachunki", null, 'T');
        categoryRepository.createTransactionCategory("Paliwo", null, 'T');
        categoryRepository.createTransactionCategory("Transport", null, 'T');
        categoryRepository.createTransactionCategory("Odzież i obuwie", null, 'T');
        categoryRepository.createTransactionCategory("Zdrowie", null, 'T');
        categoryRepository.createTransactionCategory("Artykuły przemysłowe", null, 'T');
        categoryRepository.createTransactionCategory("Wakacje", null, 'T');
        categoryRepository.createTransactionCategory("Wyposażenie mieszkania", null, 'T');
        categoryRepository.createTransactionCategory("Kosmetyki", null, 'T');
        categoryRepository.createTransactionCategory("Prezenty", null, 'T');
        categoryRepository.createTransactionCategory("Pensja", null, 'T');
        categoryRepository.createTransactionCategory("Wynajem", null, 'T');

        try {
            userService.registerNewUserAccount(new UserDto
                    .UserDtoBuilder("Jan", "Kowalski")
                    .email("jankowalski@gmail.com")
                    .phoneNumber("751-455-339")
                    .password("zaq12wsx")
                    .matchingPassword("zaq12wsx")
                    .build());
            userService.registerNewUserAccount(new UserDto
                    .UserDtoBuilder("Anna", "Nowak")
                    .email("annanowak@gmail.com")
                    .phoneNumber("667-987-123")
                    .password("qwerty1234")
                    .matchingPassword("qwerty1234")
                    .build());
        } catch (EmailExistsException e) {
            e.printStackTrace();
        }

        User jan = userRepository.getUserByEmail("jankowalski@gmail.com");
        userTypeService.changeUserType(jan.getEmail(), "premium");
        try {
            placeService.addNewUserPlace(jan.getEmail(), new PlaceDto(null, "Manekin", 'F'));
            placeService.addNewUserPlace(jan.getEmail(), new PlaceDto(null, "Pyszne.pl", 'F'));
        } catch (PlaceExistsException e) {
            e.printStackTrace();
        }
        try {
            categoryService.addNewUserCategory(jan.getEmail(), new CategoryDto(null, "Lokaty", 'F'));
            categoryService.addNewUserCategory(jan.getEmail(), new CategoryDto(null, "Kredyty", 'F'));
        } catch (TransactionCategoryExistsException e) {
            e.printStackTrace();
        }
        accountService.addNewUserAccount(new NewAccountDto(jan.getEmail(), "Konto w PKO", 0.));
        accountService.changeUserAccountName(jan.getEmail(), new ChangeAccountNameDto("Konto podstawowe", "Konto w mBanku"));
        try {
            monthlyBudgetService.addNewUserMonthlyBudget(jan.getEmail(), new NewMonthlyBudgetDto(1300.));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            transactionService.addNewUserTransaction(new TransactionDto(null, "Standardowe zakupy", jan.getEmail(), "Konto w PKO", -57., "2018-05-21", "Biedronka", "Żywność"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Standardowe zakupy", jan.getEmail(), "Konto w PKO", -11., "2018-05-13", "Biedronka", "Żywność"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Standardowe zakupy", jan.getEmail(), "Konto w PKO", -79., "2018-05-03", "Biedronka", "Żywność"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Standardowe zakupy", jan.getEmail(), "Konto w PKO", -196., "2018-05-09", "Biedronka", "Żywność"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Na imprezę", jan.getEmail(), "Konto w PKO", -123., "2018-05-11", "Tesco", "Alkohole"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Wypad do Manekina", jan.getEmail(), "Konto w PKO", -44., "2018-05-17", "Manekin", "Jedzenie na mieście"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Za wynajem pokoju", jan.getEmail(), "Konto w PKO", 250., "2018-05-02", "", "Wynajem"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Pensja z pracy za kwiecień", jan.getEmail(), "Konto w PKO", 2476., "2018-05-01", "PKO BP", "Pensja"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            monthlyBudgetService.addNewUserMonthlyBudget(jan.getEmail(), new NewMonthlyBudgetDto(null, LocalDate.now().minusMonths(1).withDayOfMonth(1).toString(), LocalDate.now().withDayOfMonth(1).minusDays(1).toString(), 1100.));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            transactionService.addNewUserTransaction(new TransactionDto(null, "Standardowe zakupy", jan.getEmail(), "Konto w mBanku", -257., "2018-04-21", "Biedronka", "Żywność"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Standardowe zakupy", jan.getEmail(), "Konto w mBanku", -211., "2018-04-13", "Biedronka", "Żywność"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Standardowe zakupy", jan.getEmail(), "Konto w mBanku", -279., "2018-04-03", "Biedronka", "Żywność"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Standardowe zakupy", jan.getEmail(), "Konto w mBanku", -196., "2018-04-09", "Biedronka", "Żywność"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Na imprezę", jan.getEmail(), "Konto w mBanku", -323., "2018-04-11", "Tesco", "Alkohole"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Wypad do Manekina", jan.getEmail(), "Konto w mBanku", -144., "2018-04-17", "Manekin", "Jedzenie na mieście"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Za wynajem pokoju", jan.getEmail(), "Konto w mBanku", 250., "2018-04-02", "", "Wynajem"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Pensja z pracy za marzec", jan.getEmail(), "Konto w mBanku", 3476., "2018-04-01", "PKO BP", "Pensja"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        User anna = userRepository.getUserByEmail("annanowak@gmail.com");
        try {
            placeService.addNewUserPlace(anna.getEmail(), new PlaceDto(null, "Pyszne.pl", 'F'));
        } catch (PlaceExistsException e) {
            e.printStackTrace();
        }
        try {
            categoryService.addNewUserCategory(anna.getEmail(), new CategoryDto(null, "Biżuteria", 'F'));
        } catch (TransactionCategoryExistsException e) {
            e.printStackTrace();
        }
        try {
            transactionService.addNewUserTransaction(new TransactionDto(null, "Standardowe zakupy", anna.getEmail(), "Konto podstawowe", -57., "2018-05-21", "Biedronka", "Żywność"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Na imprezę", anna.getEmail(), "Konto podstawowe", -123., "2018-05-11", "Tesco", "Alkohole"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Zamówienie z pyszne.pl", anna.getEmail(), "Konto podstawowe", -44., "2018-05-17", "Pyszne.pl", ""));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Bo mogę ;-)", anna.getEmail(), "Konto podstawowe", -250., "2018-05-02", "", "Biżuteria"));
            transactionService.addNewUserTransaction(new TransactionDto(null, "Pensja z pracy za kwiecień", anna.getEmail(), "Konto podstawowe", 2476., "2018-05-01", "PKO BP", "Pensja"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
