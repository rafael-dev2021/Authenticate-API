package myapp.authenticateAPI.service;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.dtos.UserDTO;
import myapp.authenticateAPI.infrastructure.exceptions.UserNotFoundException;
import myapp.authenticateAPI.repository.UserRepository;
import myapp.authenticateAPI.service.helpers.HelperValidateUser;
import myapp.authenticateAPI.service.helpers.user.HelperComponentAuthenticationUser;
import myapp.authenticateAPI.service.helpers.user.HelperComponentUserUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final HelperComponentUserUpdate helperComponentUserUpdate;
    private final HelperComponentAuthenticationUser helperAuthenticationUser;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteUser(String id) {
        findById(id);
        userRepository.deleteById(id);
    }

    public ResponseEntity<Void> processUpdateUser(String id, UserDTO user, Authentication authentication) {
        helperAuthenticationUser.validateAuthentication(authentication);
        String authenticatedUserId = helperAuthenticationUser.getAuthenticatedUserId(authentication);
        helperAuthenticationUser.validateUserAccess(id, authenticatedUserId);
        helperComponentUserUpdate.helperUpdateUserInformation(id, user);
        return ResponseEntity.noContent().build();
    }
}