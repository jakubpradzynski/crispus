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

import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Random;

/**
 * A service-type class related to users.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
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

    /**
     * Method register new user, after validate received data and generate hash of password and salt.
     * @param accountDto - data about new user
     * @return User (registered or null)
     * @throws EmailExistsException - Exception thrown after trying register already existed email.
     */
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

    /**
     * Method checks if email exists in database and if received password valid.
     * @param userLoginDto - data necessary to check while login
     * @return boolean (true if password was valid)
     */
    public boolean loginCheck(UserLoginDto userLoginDto) {
        if (!emailExist(userLoginDto.getLogin())) return false;
        try {
            if (isPasswordValid(userLoginDto)) return true;
        } catch (HashGenerationException e) {
            return false;
        }
        return false;
    }

    /**
     * Method validate received password.
     * Gets hash of password and salt and salt separately, hashing received password with salt and check if hashes are equals.
     * @param userLoginDto - data necessary to check while login
     * @return true (if password was correct)
     * @throws HashGenerationException - Exception is thrown when the hash generation fails.
     */
    protected boolean isPasswordValid(UserLoginDto userLoginDto) throws HashGenerationException {
        User user = userRepository.getUserByEmail(userLoginDto.getLogin());
        if (hashPasswordWithSalt(userLoginDto.getPassword(), user.getSalt()).equals(user.getPasswordHash())) return true;
        return false;
    }

    /**
     * Method checks if given email exists in database.
     * @param email - user's email
     * @return boolean (true if email exist's)
     */
    protected boolean emailExist(String email) {
        User user = userRepository.getUserByEmail(email);
        return user != null;
    }

    /**
     * Method genarates random salt for new user.
     * @return String (salt)
     */
    protected String generateSalt() {
        final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 20) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    /**
     * Method hashes password with salt.
     * @param password - password as string
     * @param salt - salt as string
     * @return String (hashed password with string)
     * @throws HashGenerationException - Exception is thrown when the hash generation fails.
     */
    protected String hashPasswordWithSalt(String password, String salt) throws HashGenerationException {
        return hashUtils.generateMD5(password + salt);
    }
}