package pl.jakubpradzynski.crispus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.domain.UserType;
import pl.jakubpradzynski.crispus.repositories.UserRepository;
import pl.jakubpradzynski.crispus.repositories.UserTypeRepository;

import org.springframework.transaction.annotation.Transactional;

/**
 * A service-type class related to user types.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Service
public class UserTypeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    /**
     * Method finds User class object asking User Repository for user by specific email.
     * Changes user type to new one given by name after call a function from User Type Repository.
     * @param username - user's email
     * @param newUserTypeName - name of new user type for user
     */
    @Transactional
    public void changeUserType(String username, String newUserTypeName) {
        User user = userRepository.getUserByEmail(username);
        UserType userType = userTypeRepository.getUserTypeByName(newUserTypeName);
        userRepository.changeUserType(user, userType);
    }

}
