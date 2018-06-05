package pl.jakubpradzynski.crispus.exceptions;

/**
 * An exception class.
 * The exception is thrown when the user registers an existing email.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
public class EmailExistsException extends Exception {
    public EmailExistsException() { super(); }
    public EmailExistsException(String message) { super(message); }
    public EmailExistsException(String message, Throwable cause) { super(message, cause); }
    public EmailExistsException(Throwable cause) { super(cause); }
}
