package myapp.authenticateAPI.service.helpers.user;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.dtos.UserDTO;
import myapp.authenticateAPI.infrastructure.exceptions.EmailAlreadyExistsException;
import myapp.authenticateAPI.infrastructure.exceptions.UserNotFoundException;
import myapp.authenticateAPI.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelperComponentUserUpdate {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void helperUpdateUserInformation(String id, UserDTO updatedUserDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!existingUser.getEmail().equals(updatedUserDto.email()) &&
                emailExists(updatedUserDto.email(), existingUser.getId())) {

            throw new EmailAlreadyExistsException();
        }

        updateUserData(existingUser, updatedUserDto);
        userRepository.save(existingUser);
    }

    private void updateUserData(User existingUser, UserDTO updatedUserDto) {
        updateName(existingUser, updatedUserDto.name());
        updateLastName(existingUser, updatedUserDto.lastName());
        updatePhoneNumber(existingUser, updatedUserDto.phoneNumber());
        updateBio(existingUser, updatedUserDto.bio());
        updateEmail(existingUser, updatedUserDto.email());
        updatePassword(existingUser, updatedUserDto.password());
    }


    private boolean emailExists(String email, String userIdToExclude) {
        User user = (User) userRepository.findByEmail(email);
        return user != null && !user.getId().equals(userIdToExclude);
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

    private void updatePhoneNumber(User existingUser, String newPhoneNumber) {
        if (newPhoneNumber != null && !newPhoneNumber.equals(existingUser.getPhoneNumber())) {
            existingUser.setPhoneNumber(newPhoneNumber);
        }
    }

    private void updateBio(User existingUser, String newBio) {
        if (newBio != null && !newBio.equals(existingUser.getBio())) {
            existingUser.setBio(newBio);
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
