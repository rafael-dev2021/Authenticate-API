package myapp.authenticateAPI.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Post;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.findAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable String id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<Post> createPost(@RequestBody @Valid Post post, Authentication authentication) {
        return postService.processCreatePost(post, authentication);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
            @PathVariable String id,
            @RequestBody @Valid Post post,
            @AuthenticationPrincipal User currentUser
    ) {
        return postService.processUpdatePost(id, post, currentUser);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Post> deletePost(@PathVariable String id, Authentication authentication) {
        postService.deletePost(id, authentication);
        return ResponseEntity.noContent().build();
    }
}
