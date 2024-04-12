package myapp.authenticateAPI.service;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Post;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.infrastructure.exceptions.PostNotFoundException;
import myapp.authenticateAPI.repository.PostRepository;
import myapp.authenticateAPI.service.helpers.post.*;
import myapp.authenticateAPI.service.helpers.HelperValidateUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final HelperValidateUser helperValidateUser;
    private final HelperComponentCreatePost helperComponentCreatePost;
    private final HelperComponentPostUpdate helperComponentPostUpdate;
    private final HelperComponentPostDelete helperComponentPostDelete;

    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    public Post findById(String id) {
        Optional<Post> post = postRepository.findById(id);
        return post.orElseThrow(() -> new PostNotFoundException(id));
    }

    public ResponseEntity<Post> processCreatePost(Post post, Authentication authentication) {
        User currentUser = helperValidateUser.getCurrentUser(authentication);
        helperValidateUser.validateCurrentUser(currentUser);

        Post createdPost = helperComponentCreatePost.helperCreateNewPost(post, currentUser);

        URI uri = helperComponentCreatePost.helperBuildUserUri(createdPost);

        return ResponseEntity.created(uri).body(createdPost);
    }


    public ResponseEntity<Post> processUpdatePost(String id, Post updatedpost, User currentUser) {
        helperValidateUser.validateCurrentUser(currentUser);
        Post existingPost = findById(id);
        helperComponentPostUpdate.helperValidateAuthorship(existingPost, currentUser);

        helperComponentPostUpdate.helperUpdatedPost(updatedpost);
        updatedpost.setId(id);
        helperComponentPostUpdate.helperUpdate(id, updatedpost);

        return ResponseEntity.noContent().build();
    }

    public void deletePost(String id, Authentication authentication) {
        User currentUser = helperValidateUser.getCurrentUser(authentication);
        Post existingPost = findById(id);
        helperComponentPostDelete.helperValidatePostDeletion(existingPost, currentUser);
        postRepository.deleteById(id);
    }
}