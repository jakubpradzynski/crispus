package pl.jakubpradzynski.crispus.validators.phone.number;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneCaseImpl implements
        ConstraintValidator<PhoneCase, String> {

    @Override
    public void initialize(PhoneCase constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Pattern pattern = Pattern.compile("^\\d{3}-\\d{3}-\\d{3}$");
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }
}