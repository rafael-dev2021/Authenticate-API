package myapp.authenticateAPI.service.helpers;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.infrastructure.exceptions.UserNotFoundException;
import myapp.authenticateAPI.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelperValidateUserUpdate {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void helperUpdateUserInformation(String id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        updateUserData(existingUser, updatedUser);
        userRepository.save(existingUser);
    }

    private void updateUserData(User existingUser, User updatedUser) {
        updateName(existingUser, updatedUser.getName());
        updateLastName(existingUser, updatedUser.getLastName());
        updateEmail(existingUser, updatedUser.getEmail());
        updatePassword(existingUser, updatedUser.getPassword());
    }

    private void updateName(User existingUser, String newName) {
        if (newName != null && !newName.equals(existingUser.getName())) {
            existingUser.setName(newName);
        }
    }

    private void updateLastName(User existingUser, String newLastName) {
        if (newLastName != null && !newLastName.equals(existingUser.getLastName())) {
            existingUser.setLastName(newLastName);
        }
    }

    private void updateEmail(User existingUser, String newEmail) {
        if (newEmail != null && !newEmail.equals(existingUser.getEmail())) {
            existingUser.setEmail(newEmail);
        }
    }

    private void updatePassword(User existingUser, String newPassword) {
        if (newPassword != null && !passwordEncoder.matches(newPassword, existingUser.getPassword())) {
            String encryptedPassword = passwordEncoder.encode(newPassword);
            existingUser.setPassword(encryptedPassword);
        }
    }
}
