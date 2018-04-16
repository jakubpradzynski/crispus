package pl.jakubpradzynski.crispus.validators;

import org.junit.Before;
import org.junit.Test;
import pl.jakubpradzynski.crispus.dto.UserDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MatchesPasswordTest {

    private static Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldReturnEmptyViolationsSetWithMatchesPasswords() {
        UserDto userDto = new UserDto
                .UserDtoBuilder("Test", "Test")
                .email("test@gmail.com")
                .phoneNumber("123-123-123")
                .password("1234")
                .matchingPassword("1234")
                .build();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void shouldReturnNonEmptyViolationsSetWithNonMatchesPasswords() {
        UserDto userDto = new UserDto
                .UserDtoBuilder("Test", "Test")
                .email("test@gmail.com")
                .phoneNumber("123-123-123")
                .password("1234")
                .matchingPassword("12345")
                .build();
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

}
