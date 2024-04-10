package myapp.authenticateAPI.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.dtos.AuthenticationDTO;
import myapp.authenticateAPI.dtos.RegisterDTO;
import myapp.authenticateAPI.infrastructure.security.TokenService;
import myapp.authenticateAPI.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthenticationDTO authDto) {
        return accountService.processLogin(authDto, authenticationManager);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDTO registerDto) {
        return accountService.processRegister(registerDto);
    }
}
