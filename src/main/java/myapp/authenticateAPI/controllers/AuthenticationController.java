package myapp.authenticateAPI.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.dtos.AuthenticationDTO;
import myapp.authenticateAPI.dtos.ResponseDTO;
import myapp.authenticateAPI.dtos.UserDTO;
import myapp.authenticateAPI.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final AccountService accountService;

    @Operation(
            method = "POST",
            description = "Endpoint for authentication." +
                    "If the user enters the wrong password more than 4 times, the account will be blocked.",
            summary = "Authenticate user by verifying credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "-Successfully login. Returns a token and email.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"token\": [\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoLWFwaSIsInN1YiI6InVzZXJAbG9jYWxob3N0LmNvbSIsImV4cCI6MTcxMzMxNDg1OH0.5CJqH5WbTdgc8_8y5-V4r9XlYkZ5Bhp6eHnmp2OQl\"]," +
                                            "\"email\": [\"email@localhost.com\"]" +
                                            "}"
                            ))),

            @ApiResponse(responseCode = "404", description = "-Invalid username or password.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"Invalid username or password.\"}"
                            ))),

            @ApiResponse(responseCode = "403", description = "-Account is locked.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"Account is locked.\"}"
                            ))),
    })
    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody AuthenticationDTO authDto) {
        return accountService.processLogin(authDto, authenticationManager);
    }


    @Operation(
            method = "POST",
            description = "Endpoint for user registration.",
            summary = "Register a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "-User registered successfully. Returns a token and email.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"token\": [\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoLWFwaSIsInN1YiI6InVzZXJAbG9jYWxob3N0LmNvbSIsImV4cCI6MTcxMzMxNDg1OH0.5CJqH5WbTdgc8_8y5-V4r9XlYkZ5Bhp6eHnmp2OQl\"]," +
                                            "\"email\": [\"email@localhost.com\"]" +
                                            "}"
                            ))),

            @ApiResponse(responseCode = "400", description = "-Property validation ",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{" +
                                            "\"name\": [\"Name is required.\", \"Name must be between 5 and 30 characters.\"]," +
                                            "\"lastName\": [\"Last name is required.\", \"Last name must be between 5 and 30 characters.\"]," +
                                            "\"phoneNumber\": [\"Phone number is required.\", \"Phone number must be less than 11 characters.\", \"Phone number must contain only digits.\"]," +
                                            "\"email\": [\"Email is required.\", \"Invalid email format.\", \"Email must be between 10 and 30 characters.\"]," +
                                            "\"password\": [\"Password must be strong and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.\", \"Password must be between 10 and 30 characters.\", \"Password is required.\"]" +
                                            "}"
                            ))),
            @ApiResponse(responseCode = "409", description = "-E-mail not available.",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "{\"message\":\"E-mail not available.\"}"
                            )))
    })
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody @Valid UserDTO userDto) {
        return accountService.processRegister(userDto);
    }
}
