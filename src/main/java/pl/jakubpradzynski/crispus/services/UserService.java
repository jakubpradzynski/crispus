package pl.jakubpradzynski.crispus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.domain.Category;
import pl.jakubpradzynski.crispus.dto.UserDto;
import pl.jakubpradzynski.crispus.dto.UserLoginDto;
import pl.jakubpradzynski.crispus.domain.Place;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.exceptions.EmailExistsException;
import pl.jakubpradzynski.crispus.exceptions.HashGenerationException;
import pl.jakubpradzynski.crispus.repositories.*;
import pl.jakubpradzynski.crispus.utils.HashUtils;

import javax.transaction.Transactional;
import java.util.HashSet;
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
    private CategoryRepository categoryRepository;

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
                    new HashSet<Place>(placeRepository.getAllPreDefinedPlaces()),
                    new HashSet<Category>(categoryRepository.getAllPreDefinedCategories())
            );
        } catch (HashGenerationException e) {
            e.printStackTrace();
        }

        if (user == null) return user;

        userRepository.createUser(user);
        accountRepository.createAccount(user, "Konto podstawowe", 0.);

        return user;
    }

    public boolean loginCheck(UserLoginDto userLoginDto) {
        if (!emailExist(userLoginDto.getLogin())) return false;
        try {
            if (isPasswordValid(userLoginDto)) return true;
        } catch (HashGenerationException e) {
            return false;
        }
        return false;
    }

    private boolean isPasswordValid(UserLoginDto userLoginDto) throws HashGenerationException {
        User user = userRepository.getUserByEmail(userLoginDto.getLogin());
        if (hashPasswordWithSalt(userLoginDto.getPassword(), user.getSalt()).equals(user.getPasswordHash())) return true;
        return false;
    }

    private boolean emailExist(String email) {
        User user = userRepository.getUserByEmail(email);
        return user != null;
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