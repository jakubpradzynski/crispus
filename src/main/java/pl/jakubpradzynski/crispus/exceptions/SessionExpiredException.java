package pl.jakubpradzynski.crispus.exceptions;

public class SessionExpiredException extends RuntimeException {
    public SessionExpiredException() { super(); }
    public SessionExpiredException(String message) { super(message); }
    public SessionExpiredException(String message, Throwable cause) { super(message, cause); }
    public SessionExpiredException(Throwable cause) { super(cause); }
}
