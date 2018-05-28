package pl.jakubpradzynski.crispus.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.dto.UserDto;
import pl.jakubpradzynski.crispus.exceptions.EmailExistsException;
import pl.jakubpradzynski.crispus.repositories.*;
import pl.jakubpradzynski.crispus.services.UserService;

import java.util.Date;
import java.util.Locale;

@Component
@Scope("singleton")
public class Starter implements CommandLineRunner {

    @Autowired
    public UserTypeRepository userTypeRepository;
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public UserService userService;
    @Autowired
    public PlaceRepository placeRepository;
    @Autowired
    public CategoryRepository categoryRepository;
    @Autowired
    public TransactionRepository transactionRepository;
    @Autowired
    public AccountRepository accountRepository;

    @Override
    public void run(String... args) {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.forLanguageTag("pl"));
//        userTypeRepository.createUserType("normal", 2, 5, 5);
//        userTypeRepository.createUserType("premium", 4, 15, 15);
//        placeRepository.createPlace("Biedronka", null, 'T');
//        placeRepository.createPlace("Lidl", null, 'T');
//        placeRepository.createPlace("Tesco", null, 'T');
//        placeRepository.createPlace("mBank", null, 'T');
//        placeRepository.createPlace("PKO BP", null, 'T');
//        placeRepository.createPlace("Cinema City", null, 'T');
//        categoryRepository.createTransactionCategory("Jedzenie", null, 'T');
//        categoryRepository.createTransactionCategory("Ubrania", null, 'T');
//        categoryRepository.createTransactionCategory("Napoje", null, 'T');
//        categoryRepository.createTransactionCategory("Wypłata z pracy", null, 'T');
//        try {
//            userService.registerNewUserAccount(new UserDto
//                    .UserDtoBuilder("Jan", "Kowalski")
//                    .email("jankowalski@gmail.com")
//                    .phoneNumber("751-455-339")
//                    .password("1234")
//                    .matchingPassword("1234")
//                    .build());
//        } catch (EmailExistsException e) {
//            e.printStackTrace();
//        }
//        try {
//            userService.registerNewUserAccount(new UserDto
//                    .UserDtoBuilder("Jakub", "Prądzyński")
//                    .email("jakubpradzynski@gmail.com")
//                    .phoneNumber("693-265-055")
//                    .password("1234")
//                    .matchingPassword("1234")
//                    .build());
//        } catch (EmailExistsException e) {
//            e.printStackTrace();
//        }
//        User user = userRepository.getUserByEmail("jankowalski@gmail.com");
//        if (user != null) System.out.println("jest nullem");
//        System.out.println(user);
//        System.out.println(categoryRepository.getCategoryByName("Ubrania"));
//        for (int i = 0; i < 40; i ++)
//            transactionRepository.createTransaction("Zakupy w biedronce " + i, user, accountRepository.getUserAccountByName(user, "Konto podstawowe"), 123., new Date(), placeRepository.getPlaceByName("Biedronka"), categoryRepository.getCategoryByName("Ubrania"));
//        User admin = userRepository.getUserByEmail("jakubpradzynski@gmail.com");
//        admin.setUserType(userTypeRepository.getUserTypeByName("premium"));
//        userRepository.updateUser(admin);
//        for (int i = 0; i < 40; i ++)
//            transactionRepository.createTransaction("Zakupy w biedronce " + i, admin, accountRepository.getUserAccountByName(admin, "Konto podstawowe"), 123., new Date(), placeRepository.getPlaceByName("Biedronka"), categoryRepository.getCategoryByName("Ubrania"));
//        accountRepository.createAccount(admin, "Drugie konto", 1200.);
//        for (int i = 0; i < 40; i ++)
//            transactionRepository.createTransaction("Zakupy w biedronce " + i, admin, accountRepository.getUserAccountByName(admin, "Drugie konto"), -12., new Date(), placeRepository.getPlaceByName("Lidl"), categoryRepository.getCategoryByName("Jedzenie"));
    }
}
