package myapp.authenticateAPI.user;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import myapp.authenticateAPI.domain.entities.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserTests {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void testNameNotBlank() {
        User user = new User();
        user.setLastName("Jon Doe");
        user.setEmail("test@example.com");
        user.setPassword("@Visual23k+");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Name is required.", violations.iterator().next().getMessage());
    }

    @Test
    void testNameSizeLessThanMinimum() {
        User user = new User();
        user.setName("Jon");
        user.setLastName("Jon Doe");
        user.setEmail("test@example.com");
        user.setPassword("@Visual23k+");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Name must be between 5 and 30 characters.", violations.iterator().next().getMessage());
    }

    @Test
    void testNameSizeMoreThanMaximum() {
        String test = new String(new char[31]).replace('\0', 'a');
        User user = new User();
        user.setName(test);
        user.setLastName("Jon Doe");
        user.setEmail("test@example.com");
        user.setPassword("@Visual23k+");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Name must be between 5 and 30 characters.", violations.iterator().next().getMessage());
    }

    @Test
    void testLastNameNotBlank() {
        User user = new User();
        user.setName("Jon Doe");
        user.setEmail("test@example.com");
        user.setPassword("@Visual23k+");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Last name is required.", violations.iterator().next().getMessage());
    }

    @Test
    void testLastNameSizeLessThanMinimum() {
        User user = new User();
        user.setLastName("Doe");
        user.setName("Jon Doe");
        user.setEmail("test@example.com");
        user.setPassword("@Visual23k+");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Last name must be between 5 and 30 characters.", violations.iterator().next().getMessage());
    }

    @Test
    void testLastNameSizeMoreThanMaximum() {
        String test = new String(new char[31]).replace('\0', 'a');
        User user = new User();
        user.setLastName(test);
        user.setName("Jon Doe");
        user.setEmail("test@example.com");
        user.setPassword("@Visual23k+");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Last name must be between 5 and 30 characters.", violations.iterator().next().getMessage());
    }



    @Test
    void testEmailInvalidFormat() {
        User user = new User();
        user.setEmail("invalid_email");
        user.setName("Jon Doe");
        user.setLastName("Jon Doe");
        user.setPassword("@Visual23k+");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Invalid email format.", violations.iterator().next().getMessage());
    }

    @Test
    void testEmailSizeLessThanMinimum() {
        User user = new User();
        user.setEmail("ads@b.c");
        user.setName("Jon Doe");
        user.setLastName("Jon Doe");
        user.setPassword("@Visual23k+");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Email must be between 10 and 30 characters.", violations.iterator().next().getMessage());
    }

    @Test
    void testEmailSizeMoreThanMaximum() {
        String test = new String(new char[31]).replace('\0', 'a') + "@example.com";
        User user = new User();
        user.setEmail(test);
        user.setName("Jon Doe");
        user.setLastName("Jon Doe");
        user.setPassword("@Visual23k+");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertEquals("Email must be between 10 and 30 characters.", violations.iterator().next().getMessage());
    }
}
