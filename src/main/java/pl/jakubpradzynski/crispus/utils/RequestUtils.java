package pl.jakubpradzynski.crispus.utils;

import org.springframework.validation.Errors;

public class RequestUtils {

    public static boolean isErrorOccured(Errors errors, String errorName) {
        return errors.getFieldErrors().stream().anyMatch(fieldError -> fieldError.getField().equals(errorName));
    }

}
