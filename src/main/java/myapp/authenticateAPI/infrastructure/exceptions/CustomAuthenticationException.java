package myapp.authenticateAPI.infrastructure.exceptions;

public class CustomAuthenticationException extends RuntimeException {
    public CustomAuthenticationException() {
        super("Invalid username or password.");
    }
}

