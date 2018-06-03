package pl.jakubpradzynski.crispus.utils;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

public class RequestUtilsTest {

    @Test
    public void isErrorOccuredTest() {
        Errors errors = new BeanPropertyBindingResult("", "");
        FieldError fieldError = new FieldError("test", "test", "test");
        ((BeanPropertyBindingResult) errors).addError(fieldError);
        Assert.assertTrue(RequestUtils.isErrorOccured(errors, "test"));
    }

    @Test
    public void isErrorOccuredNegativeTest() {
        Errors errors = new BeanPropertyBindingResult("", "");
        Assert.assertFalse(RequestUtils.isErrorOccured(errors, "test"));
    }

}
