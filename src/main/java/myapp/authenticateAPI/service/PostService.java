package myapp.authenticateAPI.service;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Post;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.infrastructure.exceptions.NotAuthenticatedException;
import myapp.authenticateAPI.infrastructure.exceptions.PostNotFoundException;
import myapp.authenticateAPI.repository.PostRepository;
import myapp.authenticateAPI.service.helpers.HelperValidateCreatePost;
import myapp.authenticateAPI.service.helpers.HelperValidatePostDelete;
import myapp.authenticateAPI.service.helpers.HelperValidatePostUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final HelperValidateCreatePost helperValidateCreatePost;
    private final PostRepository postRepository;
    private final HelperValidatePostUpdate helperValidatePostUpdate;
    private final HelperValidatePostDelete helperValidatePostDelete;

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public Post findById(String id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElseThrow(() -> new PostNotFoundException(id));
    }

    public ResponseEntity<Post> processCreatePost(Post post, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        validateCurrentUser(currentUser);

        Post createdPost = helperValidateCreatePost.helperCreateNewPost(post, currentUser);

        URI uri = helperValidateCreatePost.helperBuildUserUri(createdPost);

        return ResponseEntity.created(uri).body(createdPost);
    }


    public ResponseEntity<Post> processUpdatePost(String id, Post post, User currentUser) {
        validateCurrentUser(currentUser);
        Post existingPost = findById(id);
        helperValidatePostUpdate.helperValidateAuthorship(existingPost, currentUser);

        Post postUpdate = helperValidatePostUpdate.helperCreateUpdatedPost(post);
        postUpdate.setId(id);
        helperValidatePostUpdate.helperUpdate(id, postUpdate);

        return ResponseEntity.noContent().build();
    }

    public void deletePost(String id, Authentication authentication) {
        User currentUser = getCurrentUser(authentication);
        Post existingPost = findById(id);
        helperValidatePostDelete.helperValidatePostDeletion(existingPost, currentUser);
        postRepository.deleteById(id);
    }

    private void validateCurrentUser(User currentUser) {
        if (currentUser == null) {
            throw new NotAuthenticatedException();
        }
    }

    private User getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotAuthenticatedException();
        }
        return (User) authentication.getPrincipal();
    }
}