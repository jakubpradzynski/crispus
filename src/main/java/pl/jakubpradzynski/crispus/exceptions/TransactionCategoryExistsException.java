package pl.jakubpradzynski.crispus.exceptions;

public class TransactionCategoryExistsException extends Exception {
    public TransactionCategoryExistsException() { super(); }
    public TransactionCategoryExistsException(String message) { super(message); }
    public TransactionCategoryExistsException(String message, Throwable cause) { super(message, cause); }
    public TransactionCategoryExistsException(Throwable cause) { super(cause); }
}
