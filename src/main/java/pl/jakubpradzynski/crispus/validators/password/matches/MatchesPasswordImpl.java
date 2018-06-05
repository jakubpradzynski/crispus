package pl.jakubpradzynski.crispus.validators.password.matches;

import pl.jakubpradzynski.crispus.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * A validator-type class for validate if password matches.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
public class MatchesPasswordImpl implements
        ConstraintValidator<MatchesPassword, UserDto> {

    @Override
    public void initialize(MatchesPassword constraintAnnotation) {
    }

    /**
     * Override methods checks if passwords are equals.
     * @param user - user data with passwords to check
     * @param context - ConstraintValidatorContext
     * @return boolean (true if passwords matches)
     */
    @Override
    public boolean isValid(UserDto user, ConstraintValidatorContext context){
        return user.getPassword().equals(user.getMatchingPassword());
    }
}