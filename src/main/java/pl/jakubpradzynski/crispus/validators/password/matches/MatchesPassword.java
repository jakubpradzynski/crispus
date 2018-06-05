package pl.jakubpradzynski.crispus.validators.password.matches;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * A validator-type interface for validate if password matches.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@Target( {ElementType.TYPE, ElementType.ANNOTATION_TYPE} )
@Retention( RetentionPolicy.RUNTIME)
@Constraint( validatedBy = MatchesPasswordImpl.class)
@Documented
public @interface MatchesPassword {
    String message() default
            "Hasła nie pasują!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}