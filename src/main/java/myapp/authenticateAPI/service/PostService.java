package myapp.authenticateAPI.service;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Post;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.dtos.AuthorDTO;
import myapp.authenticateAPI.infrastructure.exceptions.DomainAccessDeniedException;
import myapp.authenticateAPI.infrastructure.exceptions.NotAuthenticatedException;
import myapp.authenticateAPI.infrastructure.exceptions.PostNotFoundException;
import myapp.authenticateAPI.repository.PostRepository;
import myapp.authenticateAPI.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public Post findById(String id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElseThrow(() -> new PostNotFoundException(id));
    }


    public ResponseEntity<Post> processCreatePost(Post post, Authentication authentication) {
        // Checks if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException();
        }

        User currentUser = (User) authentication.getPrincipal();
        post.setDate(LocalDateTime.now());
        post.setUser(currentUser);

        // Defines the author of the post as a new AuthorDTO based on the current user
        post.setAuthor(new AuthorDTO(currentUser.getName(), currentUser.getLastName()));

        Post createdPost = postRepository.insert(post);

        // Adds the post to the current user's list of posts
        currentUser.getPosts().add(createdPost);
        userRepository.save(currentUser);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdPost.getId()).toUri();

        return ResponseEntity.created(uri).body(createdPost);
    }


    public ResponseEntity<Post> processUpdatePost(String id, Post post, User currentUser) {
        if (currentUser == null) {
            throw new NotAuthenticatedException();
        }

        Post existingPost = findById(id);

        // Check if the current user is the author of the post
        if (!existingPost.getUser().getId().equals(currentUser.getId())) {
            // Return 403 Forbidden if the user is not the author of the post
            throw new DomainAccessDeniedException();
        }
        // If the current user is the author of the post, update the post
        Post postUpdate = updatePost(post);
        postUpdate.setId(id);
        update(id, postUpdate);
        return ResponseEntity.noContent().build();
    }

    private Post updatePost(Post post) {
        return new Post(post.getTitle(), post.getBody(), LocalDateTime.now());
    }

    private void update(String id, Post updatedPost) {

        // Find the existing post in the database based on the given ID
        Post existingPost = findById(id);

        // Check that the "title" field was supplied in the request and that the new value is different from the current value in the database
        if (updatedPost.getTitle() != null && !updatedPost.getTitle().equals(existingPost.getTitle())) {
            // If the title provided in the request is different from the current title in the database, update the post title
            existingPost.setTitle(updatedPost.getTitle());
        }
        // Check that the "body" field was supplied in the request and that the new value is different from the current value in the database
        if (updatedPost.getBody() != null && !updatedPost.getBody().equals(existingPost.getBody())) {
            // If the body supplied in the request is different from the current body in the database, update the body of the post
            existingPost.setBody(updatedPost.getBody());
        }

        // Automatically set the post-update date
        existingPost.setDate(LocalDateTime.now());

        // Save the changes made to the existing post in the database
        postRepository.save(existingPost);
    }

    public void deletePost(String id, Authentication authentication) {
        // Checks if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException();
        }

        // Get the currently authenticated user
        User currentUser = (User) authentication.getPrincipal();

        // Find the post by ID
        Post existingPost = findById(id);

        // Check if the post exists
        if (existingPost == null) {
            throw new PostNotFoundException(id);
        }

        // Checks if the authenticated user is the author of the post
        if (!existingPost.getUser().getId().equals(currentUser.getId())) {
            throw new DomainAccessDeniedException();
        }

        // If everything is correct, delete the post
        postRepository.deleteById(id);
    }
}
