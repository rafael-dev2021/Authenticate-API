package myapp.authenticateAPI.service;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.infrastructure.exceptions.DomainAccessDeniedException;
import myapp.authenticateAPI.infrastructure.exceptions.NotAuthenticatedException;
import myapp.authenticateAPI.infrastructure.exceptions.UserNotFoundException;
import myapp.authenticateAPI.repository.UserRepository;
import myapp.authenticateAPI.service.helpers.HelperValidateUserUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final HelperValidateUserUpdate helperValidateUserUpdate;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findById(String id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new UserNotFoundException(id));
    }

    public void deleteUser(String id, Authentication authentication) {

        validateAuthentication(authentication);

        String authenticatedUserId = getAuthenticatedUserId(authentication);

        boolean isAdmin = isAdmin(authentication);

        if (!isAdmin && !id.equals(authenticatedUserId)) {
            throw new DomainAccessDeniedException();
        }

        findById(id);
        userRepository.deleteById(id);
    }

    public ResponseEntity<Void> processUpdateUser(String id, User user, Authentication authentication) {
        validateAuthentication(authentication);
        String authenticatedUserId = getAuthenticatedUserId(authentication);
        validateUserAccess(id, authenticatedUserId);
        helperValidateUserUpdate.helperUpdateUserInformation(id, user);
        return ResponseEntity.noContent().build();
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
    private void validateAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException();
        }
    }

    private String getAuthenticatedUserId(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }

    private void validateUserAccess(String id, String authenticatedUserId) {
        if (!id.equals(authenticatedUserId)) {
            throw new DomainAccessDeniedException();
        }
    }
}