package pl.jakubpradzynski.crispus.exceptions;

public class PlaceExistsException extends Exception {
    public PlaceExistsException() { super(); }
    public PlaceExistsException(String message) { super(message); }
    public PlaceExistsException(String message, Throwable cause) { super(message, cause); }
    public PlaceExistsException(Throwable cause) { super(cause); }
}
