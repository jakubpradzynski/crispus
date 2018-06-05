package pl.jakubpradzynski.crispus.validators.phone.number;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * A validator-type interface for validate if phone number has correct format.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Target( {ElementType.FIELD, ElementType.METHOD} )
@Retention( RetentionPolicy.RUNTIME)
@Constraint( validatedBy = PhoneCaseImpl.class)
@Documented
public @interface PhoneCase {
    String message() default
            "Numer telefonu musi być postaci: xxx-xxx-xxx";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}