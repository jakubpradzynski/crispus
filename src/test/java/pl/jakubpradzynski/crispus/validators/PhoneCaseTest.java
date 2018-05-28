package pl.jakubpradzynski.crispus.validators;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;
import pl.jakubpradzynski.crispus.domain.User;
import pl.jakubpradzynski.crispus.domain.UserType;

public class PhoneCaseTest {

    private static Validator validator;
    private static User user;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        user = new User();
        user.setId(1);
        user.setName("test");
        user.setSurname("test");
        user.setEmail("test@gmail.com");
        user.setPasswordHash("test");
        user.setSalt("test");
        user.setUserType(new UserType());
        user.setPlaces(Collections.EMPTY_SET);
        user.setTransactionCategories(Collections.EMPTY_SET);
    }

    @Test
    public void shouldReturnEmptyViolationsSetWithCorrectPhoneNumber() {
        user.setPhoneNumber("123-123-123");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldReturnNonEmptyViolationsSetWithIncorrectPhoneNumber() {
        user.setPhoneNumber("123-1234-123");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
