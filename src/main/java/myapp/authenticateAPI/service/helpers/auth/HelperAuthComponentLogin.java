package myapp.authenticateAPI.service.helpers.auth;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.dtos.AuthenticationDTO;
import myapp.authenticateAPI.dtos.ResponseDTO;
import myapp.authenticateAPI.infrastructure.exceptions.AccountLockedException;
import myapp.authenticateAPI.infrastructure.exceptions.CustomAuthenticationException;
import myapp.authenticateAPI.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelperAuthComponentLogin {

    private final UserRepository userRepository;

    public Authentication authenticateUser(AuthenticationDTO authDto, AuthenticationManager authManager) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());
        return authManager.authenticate(usernamePassword);
    }

    public void handleSuccessfulLogin(User user) {
        if (!user.isActive()) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            userRepository.save(user);
            if (user.getFailedLoginAttempts() >= 4) {
                user.lockAccountForHours();
                userRepository.save(user);
            }
            throw new LockedException("Account is locked.");
        }
        user.setFailedLoginAttempts(0);
        userRepository.save(user);
    }

    public ResponseEntity<ResponseDTO> handleLockedAccount() {
        throw new AccountLockedException();
    }

    public ResponseEntity<ResponseDTO> handleBadCredentials(String email) {
        var user = (User) userRepository.findByEmail(email);
        if (user != null) {
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            userRepository.save(user);
            if (user.getFailedLoginAttempts() >= 4) {
                user.lockAccountForHours();
                userRepository.save(user);
            }
        }
        throw new CustomAuthenticationException();
    }
}
