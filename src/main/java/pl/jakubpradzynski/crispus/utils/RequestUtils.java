package pl.jakubpradzynski.crispus.utils;

import org.springframework.validation.Errors;

/**
 * A utils-type class with helpful methods related to requests.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
public class RequestUtils {

    /**
     * Method checks whether an error with a given name occurred in the errors.
     * @param errors - Errors
     * @param errorName - error name
     * @return boolean (true if error occurred)
     */
    public static boolean isErrorOccurred(Errors errors, String errorName) {
        return errors.getFieldErrors().stream().anyMatch(fieldError -> fieldError.getField().equals(errorName));
    }

}
