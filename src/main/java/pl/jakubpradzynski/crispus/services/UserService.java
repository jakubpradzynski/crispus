package pl.jakubpradzynski.crispus.services;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.api.dto.UserDto;
import pl.jakubpradzynski.crispus.domain.Place;
import pl.jakubpradzynski.crispus.domain.TransactionCategory;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.exceptions.EmailExistsException;
import pl.jakubpradzynski.crispus.exceptions.HashGenerationException;
import pl.jakubpradzynski.crispus.repositories.*;
import pl.jakubpradzynski.crispus.utils.HashUtils;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private TransactionCategoryRepository transactionCategoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    private HashUtils hashUtils;

    @Transactional
    public User registerNewUserAccount(UserDto accountDto)
            throws EmailExistsException {

        if (emailExist(accountDto.getEmail())) {
            throw new EmailExistsException(
                    "There is an account with that email adress: "
                            +  accountDto.getEmail());
        }

        String salt = generateSalt();
        User user = null;

        try {
            user = new User(
                    accountDto.getEmail(),
                    hashPasswordWithSalt(accountDto.getPassword(), salt),
                    salt,
                    accountDto.getName(),
                    accountDto.getSurname(),
                    accountDto.getPhoneNumber(),
                    userTypeRepository.getUserTypeByName("normal"),
                    (Set<Place>)placeRepository.getAllPreDefinedPlaces(),
                    (Set<TransactionCategory>)transactionCategoryRepository.getAllPreDefinedTransactionCategories()
            );
        } catch (HashGenerationException e) {
            e.printStackTrace();
        }

        if (user == null) return user;

        userRepository.createUser(user);
        accountRepository.createAccount(user, "Konto podstawowe", 0.);

        return user;
    }

    private boolean emailExist(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    private String generateSalt() {
        final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 20) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    private String hashPasswordWithSalt(String password, String salt) throws HashGenerationException {
        return hashUtils.generateMD5(password + salt);
    }


}