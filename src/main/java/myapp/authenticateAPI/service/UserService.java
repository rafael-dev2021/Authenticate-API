package myapp.authenticateAPI.service;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.infrastructure.exceptions.DomainAccessDeniedException;
import myapp.authenticateAPI.infrastructure.exceptions.NotAuthenticatedException;
import myapp.authenticateAPI.infrastructure.exceptions.UserNotFoundException;
import myapp.authenticateAPI.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new UserNotFoundException(id));
    }


    public void deleteUser(String id, Authentication authentication) {
        // Checks if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException();
        }

        // Get the authenticated user's ID
        String authenticatedUserId = ((User) authentication.getPrincipal()).getId();

        // Check if the authenticated user is an admin
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // If the authenticated user is not an admin and is not deleting their own account
        if (!isAdmin && !id.equals(authenticatedUserId)) {
            throw new DomainAccessDeniedException();
        }

        // Delete the user
        findById(id); // This line doesn't seem necessary
        userRepository.deleteById(id);
    }



    public ResponseEntity<Void> processUpdateUser(String id, User user, Authentication authentication) {
        // Checks if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException();
        }

        // Get the authenticated user's ID
        String authenticatedUserId = ((User) authentication.getPrincipal()).getId();

        // Check if the authenticated user is trying to update their own information
        if (!id.equals(authenticatedUserId)) {
            // If not, throw access denied exception
            throw new DomainAccessDeniedException();
        }

        update(id, user);
        return ResponseEntity.noContent().build();
    }

    private void update(String id, User updatedUser) {

        User existingUser = findById(id);

        updateUser(existingUser, updatedUser);

        // Save the changes made to the existing user in the database
        userRepository.save(existingUser);
    }

    private void updateUser(User existingUser, User updatedUser) {
        // Check if the "name" field was provided in the request and if the new value is different from the current value in the database
        if (updatedUser.getName() != null && !updatedUser.getName().equals(existingUser.getName())) {
            // If the name provided in the request is different from the current name in the database, update the user's name
            existingUser.setName(updatedUser.getName());
        }

        // Check if the "lastName" field was provided in the request and if the new value is different from the current value in the database
        if (updatedUser.getLastName() != null && !updatedUser.getLastName().equals(existingUser.getLastName())) {
            // If the last name provided in the request is different from the current last name in the database, update the user's last name
            existingUser.setLastName(updatedUser.getLastName());
        }

        // Check if the "email" field was provided in the request and if the new value is different from the current value in the database
        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())) {
            // If the email provided in the request is different from the current email in the database, update the user's email
            existingUser.setEmail(updatedUser.getEmail());
        }

        // Check if the "password" field was provided in the request
        if (updatedUser.getPassword() != null) {
            // Get the new password provided in the request
            String newPassword = updatedUser.getPassword();

            // Check if the new password is different from the current password in the database
            if (!passwordEncoder.matches(newPassword, existingUser.getPassword())) {
                // If the new password is different from the current password in the database, encrypt the new password
                String encryptedPassword = passwordEncoder.encode(newPassword);
                // Update the user's password with the new encrypted password
                existingUser.setPassword(encryptedPassword);
            }
        }
    }
}
