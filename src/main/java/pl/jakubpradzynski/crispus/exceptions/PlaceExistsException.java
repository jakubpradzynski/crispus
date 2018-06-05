package pl.jakubpradzynski.crispus.exceptions;

/**
 * An exception class.
 * The exception is thrown when the user tries to create a place that already exists.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
public class PlaceExistsException extends Exception {
    public PlaceExistsException() { super(); }
    public PlaceExistsException(String message) { super(message); }
    public PlaceExistsException(String message, Throwable cause) { super(message, cause); }
    public PlaceExistsException(Throwable cause) { super(cause); }
}
