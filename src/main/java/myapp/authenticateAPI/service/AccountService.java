package myapp.authenticateAPI.service;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.domain.entities.UserRole;
import myapp.authenticateAPI.dtos.AuthenticationDTO;
import myapp.authenticateAPI.dtos.ResponseDTO;
import myapp.authenticateAPI.dtos.UserDTO;
import myapp.authenticateAPI.infrastructure.exceptions.EmailAlreadyExistsException;
import myapp.authenticateAPI.infrastructure.security.TokenService;
import myapp.authenticateAPI.repository.UserRepository;
import myapp.authenticateAPI.service.helpers.auth.HelperAuthComponentLogin;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final HelperAuthComponentLogin helperAuthComponent;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public ResponseEntity<ResponseDTO> processLogin(AuthenticationDTO authDto, AuthenticationManager authManager) {
        try {
            var authResult = helperAuthComponent.authenticateUser(authDto, authManager);
            var user = (User) authResult.getPrincipal();

            helperAuthComponent.handleSuccessfulLogin(user);

            var token = tokenService.generateToken(user);
            return ResponseEntity.ok(new ResponseDTO(token, user.getEmail()));

        } catch (LockedException e) {
            return helperAuthComponent.handleLockedAccount();

        } catch (BadCredentialsException e) {
            return helperAuthComponent.handleBadCredentials(authDto.email());
        }
    }


    public ResponseEntity<ResponseDTO> processRegister(UserDTO userDto) {
        if (emailExists(userDto.email())) {
            throw new EmailAlreadyExistsException();
        }
        User newUser = createUser(userDto);
        User savedUser = userRepository.insert(newUser);
        URI uri = buildUserUri(savedUser);

        String token = tokenService.generateToken(savedUser);

        return ResponseEntity.created(uri).body(new ResponseDTO(token, savedUser.getEmail()));
    }


    private boolean emailExists(String email) {
        return loadUserByUsername(email) != null;
    }

    private User createUser(UserDTO userDto) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(userDto.password());
        UserRole role = UserRole.USER;
        return new User(
                userDto.name(),
                userDto.lastName(),
                userDto.phoneNumber(),
                userDto.bio(),
                userDto.email(),
                encryptedPassword,
                true,
                role
        );
    }


    private URI buildUserUri(User user) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
    }
}
