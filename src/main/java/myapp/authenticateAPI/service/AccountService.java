package myapp.authenticateAPI.service;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.dtos.AuthenticationDTO;
import myapp.authenticateAPI.dtos.RegisterDTO;
import myapp.authenticateAPI.dtos.ResponseDTO;
import myapp.authenticateAPI.infrastructure.exceptions.CustomAuthenticationException;
import myapp.authenticateAPI.infrastructure.security.TokenService;
import myapp.authenticateAPI.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username);
    }

    public ResponseEntity<?> processLogin(AuthenticationDTO authDto, AuthenticationManager authManager) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());
            var auth = authManager.authenticate(usernamePassword);
            var token = tokenService.generateToken((User) auth.getPrincipal());
            return ResponseEntity.ok(new ResponseDTO(token));

        } catch (AuthenticationException e) {
            throw new CustomAuthenticationException();
        }
    }


    public ResponseEntity<?> processRegister(RegisterDTO registerDto) {
        if (emailExists(registerDto.email())) {
            return ResponseEntity.badRequest().body("Email already exists.");
        }
        User newUser = createUser(registerDto);
        User savedUser = userRepository.insert(newUser);
        URI uri = buildUserUri(savedUser);

        return ResponseEntity.created(uri).build();
    }

    private boolean emailExists(String email) {
        return loadUserByUsername(email) != null;
    }

    private User createUser(RegisterDTO registerDto) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(registerDto.password());
        return new User(registerDto.name(), registerDto.lastName(), registerDto.email(), encryptedPassword);
    }

    private URI buildUserUri(User user) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
    }
}
