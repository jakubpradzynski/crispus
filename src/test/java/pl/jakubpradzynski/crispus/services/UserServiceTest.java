package pl.jakubpradzynski.crispus.services;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import pl.jakubpradzynski.crispus.exceptions.HashGenerationException;
import pl.jakubpradzynski.crispus.repositories.*;
import pl.jakubpradzynski.crispus.utils.HashUtils;

@RunWith(SpringRunner.class)
public class UserServiceTest {

    @TestConfiguration
    static class UserServiceTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserService();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserTypeRepository userTypeRepository;

    @MockBean
    private PlaceRepository placeRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private AccountRepository accountRepository;

    private HashUtils hashUtils;

    @Test
    public void saltGeneratorTest() {
        String salt = userService.generateSalt();
        Assert.assertTrue(salt.length() == 20);
    }

    @Test
    public void hashPasswordWithSaltTest() {
        String hash = null;
        try {
            hash = userService.hashPasswordWithSalt("test", "test");
        } catch (HashGenerationException e) {
            e.printStackTrace();
        }
        String correctHash = "05a671c66aefea124cc08b76ea6d30bb";
        Assert.assertTrue(hash.equals(correctHash));
    }

}
