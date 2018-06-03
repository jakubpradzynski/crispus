package pl.jakubpradzynski.crispus.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import pl.jakubpradzynski.crispus.exceptions.PlaceExistsException;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;
import pl.jakubpradzynski.crispus.exceptions.TransactionCategoryExistsException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandlingController {

    @ExceptionHandler(SessionExpiredException.class)
    public ModelAndView handleError(HttpServletRequest requset, SessionExpiredException exception) {
        return getModelAndView(requset, exception);
    }

    @ExceptionHandler(PlaceExistsException.class)
    public ModelAndView handleError(HttpServletRequest requset, PlaceExistsException exception) {
        return getModelAndView(requset, exception);
    }

    @ExceptionHandler(TransactionCategoryExistsException.class)
    public ModelAndView handleError(HttpServletRequest requset, TransactionCategoryExistsException exception) {
        return getModelAndView(requset, exception);
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest requset, Exception exception) {
        return getModelAndView(requset, exception);
    }

    private ModelAndView getModelAndView(HttpServletRequest requset, Exception exception) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("exception", exception);
        mav.addObject("url", requset.getRequestURL());
        return mav;
    }
}
