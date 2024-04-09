package myapp.authenticateAPI.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Post;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findByIdUser(@PathVariable String id) {
        User user = userService.findById(id);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUser(
            @PathVariable String id,
            @RequestBody @Valid User user,
            Authentication authentication
    ) {
        return userService.processUpdateUser(id, user, authentication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id, Authentication authentication) {
        userService.deleteUser(id, authentication);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<Post>> findAllPosts(@PathVariable String id) {
        User obj = userService.findById(id);
        return ResponseEntity.ok().body(obj.getPosts());
    }
}
