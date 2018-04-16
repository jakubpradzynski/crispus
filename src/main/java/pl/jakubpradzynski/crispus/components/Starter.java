package pl.jakubpradzynski.crispus.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import pl.jakubpradzynski.crispus.dto.UserDto;
import pl.jakubpradzynski.crispus.exceptions.EmailExistsException;
import pl.jakubpradzynski.crispus.repositories.PlaceRepository;
import pl.jakubpradzynski.crispus.repositories.TransactionCategoryRepository;
import pl.jakubpradzynski.crispus.repositories.UserRepository;
import pl.jakubpradzynski.crispus.repositories.UserTypeRepository;
import pl.jakubpradzynski.crispus.services.UserService;

import java.util.Locale;

@Component
@Scope("singleton")
public class Starter implements CommandLineRunner {

    @Autowired public UserTypeRepository userTypeRepository;
    @Autowired public UserRepository userRepository;
    @Autowired public UserService userService;
    @Autowired public PlaceRepository placeRepository;
    @Autowired public TransactionCategoryRepository transactionCategoryRepository;

    @Override
    public void run(String... args) {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.forLanguageTag("pl"));
        userTypeRepository.createUserType("normal", 2, 5, 5);
        userTypeRepository.createUserType("premium", 4, 15, 15);
        placeRepository.createPlace("Biedronka", null);
        placeRepository.createPlace("Lidl", null);
        placeRepository.createPlace("Tesco", null);
        placeRepository.createPlace("mBank", null);
        placeRepository.createPlace("PKO BP", null);
        placeRepository.createPlace("Cinema City", null);
        transactionCategoryRepository.createTransactionCategory("Jedzenie", null);
        transactionCategoryRepository.createTransactionCategory("Ubrania", null);
        transactionCategoryRepository.createTransactionCategory("Napoje", null);
        transactionCategoryRepository.createTransactionCategory("Wypłata z pracy", null);
        try {
            userService.registerNewUserAccount(new UserDto
                    .UserDtoBuilder("Jan", "Kowalski")
                    .email("jankowalski@gmail.com")
                    .phoneNumber("751-455-339")
                    .password("1234")
                    .matchingPassword("1234")
                    .build());
        } catch (EmailExistsException e) {
            e.printStackTrace();
        }
    }
}
