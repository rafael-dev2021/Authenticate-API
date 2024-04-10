package myapp.authenticateAPI.service.helpers;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Post;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.infrastructure.exceptions.DomainAccessDeniedException;
import myapp.authenticateAPI.infrastructure.exceptions.PostNotFoundException;
import myapp.authenticateAPI.repository.PostRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HelperValidatePostUpdate {

    private final PostRepository postRepository;

    public void helperUpdate(String id, Post updatedPost) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        helperUpdateTitle(existingPost, updatedPost.getTitle());
        helperUpdateBody(existingPost, updatedPost.getBody());

        existingPost.setDate(LocalDateTime.now());

        postRepository.save(existingPost);
    }

    public void helperValidateAuthorship(Post existingPost, User currentUser) {
        if (!existingPost.getUser().getId().equals(currentUser.getId())) {
            throw new DomainAccessDeniedException();
        }
    }

    public Post helperCreateUpdatedPost(Post post) {
        return new Post(post.getTitle(), post.getBody(), LocalDateTime.now());
    }

    private void helperUpdateTitle(Post existingPost, String newTitle) {
        if (newTitle != null && !newTitle.equals(existingPost.getTitle())) {
            existingPost.setTitle(newTitle);
        }
    }

    private void helperUpdateBody(Post existingPost, String newBody) {
        if (newBody != null && !newBody.equals(existingPost.getBody())) {
            existingPost.setBody(newBody);
        }
    }
}
