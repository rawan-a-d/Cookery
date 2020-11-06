package service.repository;

public class CookeryDatabaseException extends Exception {
    public CookeryDatabaseException(String message) {
        super(message);
    }

    public CookeryDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
