package pl.jakubpradzynski.crispus.exceptions;

/**
 * An exception class.
 * The exception is thrown when the session has expired.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException() { super(); }
    public SessionExpiredException(String message) { super(message); }
    public SessionExpiredException(String message, Throwable cause) { super(message, cause); }
    public SessionExpiredException(Throwable cause) { super(cause); }
}
