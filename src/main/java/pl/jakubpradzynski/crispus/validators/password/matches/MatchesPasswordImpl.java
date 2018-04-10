package pl.jakubpradzynski.crispus.validators.password.matches;

import pl.jakubpradzynski.crispus.api.dto.UserDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MatchesPasswordImpl implements
        ConstraintValidator<MatchesPassword, Object> {

    @Override
    public void initialize(MatchesPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        UserDto user = (UserDto) obj;
        return user.getPassword().equals(user.getMatchingPassword());
    }
}