package pl.jakubpradzynski.crispus.exceptions;

/**
 * An exception class.
 * The exception is thrown when the user tries to create a category that already exists.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
public class CategoryExistsException extends Exception {
    public CategoryExistsException() { super(); }
    public CategoryExistsException(String message) { super(message); }
    public CategoryExistsException(String message, Throwable cause) { super(message, cause); }
    public CategoryExistsException(Throwable cause) { super(cause); }
}
