package myapp.authenticateAPI.infrastructure.exceptions;

import lombok.Getter;
import lombok.Setter;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(String id) {
        super("Could not find user with id:" + id);
    }
}
