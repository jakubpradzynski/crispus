package pl.jakubpradzynski.crispus.exceptions;

public class HashGenerationException extends Exception {
    public HashGenerationException() { super(); }
    public HashGenerationException(String message) { super(message); }
    public HashGenerationException(String message, Throwable cause) { super(message, cause); }
    public HashGenerationException(Throwable cause) { super(cause); }
}
