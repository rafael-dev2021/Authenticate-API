package myapp.authenticateAPI.infrastructure.exceptions;

public class AccountLockedException extends RuntimeException {
    public AccountLockedException() {
        super("Account is locked.");
    }
}
