package pl.jakubpradzynski.crispus.validators.password.matches;

import pl.jakubpradzynski.crispus.api.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MatchesPasswordImpl implements
        ConstraintValidator<MatchesPassword, UserDto> {

    @Override
    public void initialize(MatchesPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(UserDto user, ConstraintValidatorContext context){
        return user.getPassword().equals(user.getMatchingPassword());
    }
}