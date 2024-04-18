package myapp.authenticateAPI.service.helpers.user;

import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.infrastructure.exceptions.DomainAccessDeniedException;
import myapp.authenticateAPI.infrastructure.exceptions.NotAuthenticatedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class HelperComponentAuthenticationUser {

    public void validateAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException();
        }
    }

    public String getAuthenticatedUserId(Authentication authentication) {
        return ((User) authentication.getPrincipal()).getId();
    }

    public void validateUserAccess(String id, String authenticatedUserId) {
        if (!id.equals(authenticatedUserId)) {
            throw new DomainAccessDeniedException();
        }
    }
}
