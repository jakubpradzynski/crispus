package pl.jakubpradzynski.crispus.exceptions;

/**
 * An exception class.
 * The exception is thrown when the password and salt hash generation fails for the user.
 *
 * @author Jakub Prądzyński
 * @version 1.0
 * @since 03.06.2018r.
 */
public class HashGenerationException extends Exception {
    public HashGenerationException() { super(); }
    public HashGenerationException(String message) { super(message); }
    public HashGenerationException(String message, Throwable cause) { super(message, cause); }
    public HashGenerationException(Throwable cause) { super(cause); }
}
