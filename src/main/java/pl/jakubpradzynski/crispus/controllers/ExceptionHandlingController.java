package pl.jakubpradzynski.crispus.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import pl.jakubpradzynski.crispus.exceptions.CategoryExistsException;
import pl.jakubpradzynski.crispus.exceptions.PlaceExistsException;
import pl.jakubpradzynski.crispus.exceptions.SessionExpiredException;

import javax.servlet.http.HttpServletRequest;

/**
 * A controller-type class for handling exceptions.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
@ControllerAdvice
public class ExceptionHandlingController {

    /**
     * Method to handle SessionExpiredException
     * @param request - HttpServletRequest
     * @param exception - SessionExpiredException
     * @return Model and View (error.html)
     */
    @ExceptionHandler(SessionExpiredException.class)
    public ModelAndView handleError(HttpServletRequest request, SessionExpiredException exception) {
        return getModelAndView(request, exception);
    }

    /**
     * Method to handle PlaceExistsException
     * @param request - HttpServletRequest
     * @param exception - PlaceExistsException
     * @return Model and View (error.html)
     */
    @ExceptionHandler(PlaceExistsException.class)
    public ModelAndView handleError(HttpServletRequest request, PlaceExistsException exception) {
        return getModelAndView(request, exception);
    }

    /**
     * Method to handle CategoryExistsException
     * @param request - HttpServletRequest
     * @param exception - CategoryExistsException
     * @return Model and View (error.html)
     */
    @ExceptionHandler(CategoryExistsException.class)
    public ModelAndView handleError(HttpServletRequest request, CategoryExistsException exception) {
        return getModelAndView(request, exception);
    }

    /**
     * Method to handle any Exception
     * @param request - HttpServletRequest
     * @param exception - Exception
     * @return Model and View (error.html)
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest request, Exception exception) {
        return getModelAndView(request, exception);
    }

    /**
     * Private method which create ModelAndView and add exception object to model.
     * @param request - HttpServletRequest
     * @param exception - Exception
     * @return Model and View (error.html)
     */
    private ModelAndView getModelAndView(HttpServletRequest request, Exception exception) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("exception", exception);
        mav.addObject("url", request.getRequestURL());
        return mav;
    }
}
