package pl.jakubpradzynski.crispus.validators.phone.number;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A validator-type class for validate if phone number has correct format.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
public class PhoneCaseImpl implements
        ConstraintValidator<PhoneCase, String> {

    @Override
    public void initialize(PhoneCase constraintAnnotation) {
    }

    /**
     * Override method checks if phone number format match to xxx-xxx-xxx.
     * @param value - phone number
     * @param context - ConstraintValidatorContext
     * @return boolean (true is phone number format match to xxx-xxx-xxx)
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value.equals("")) return true;
        Pattern pattern = Pattern.compile("^\\d{3}-\\d{3}-\\d{3}$");
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }
}