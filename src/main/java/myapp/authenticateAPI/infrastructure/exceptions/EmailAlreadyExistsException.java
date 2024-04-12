package myapp.authenticateAPI.infrastructure.exceptions;

public class EmailAlreadyExistsException  extends RuntimeException {
    public EmailAlreadyExistsException() {
        super("Email already exists.");
    }
}
