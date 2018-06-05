package pl.jakubpradzynski.crispus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.domain.UserType;
import pl.jakubpradzynski.crispus.repositories.UserRepository;
import pl.jakubpradzynski.crispus.repositories.UserTypeRepository;

import org.springframework.transaction.annotation.Transactional;

@Service
public class UserTypeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTypeRepository userTypeRepository;

    @Transactional
    public void changeUserType(String username, String newUserTypeName) {
        User user = userRepository.getUserByEmail(username);
        UserType userType = userTypeRepository.getUserTypeByName(newUserTypeName);
        userRepository.changeUserType(user, userType);
    }

}
