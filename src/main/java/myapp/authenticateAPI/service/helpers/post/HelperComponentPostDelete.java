package myapp.authenticateAPI.service.helpers.post;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Post;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.infrastructure.exceptions.DomainAccessDeniedException;
import myapp.authenticateAPI.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HelperComponentPostDelete {

    private final UserRepository userRepository;


    public void helperValidatePostDeletion(Post existingPost, User currentUser) {
        if (!existingPost.getUser().getId().equals(currentUser.getId())) {
            throw new DomainAccessDeniedException();
        }
    }

    public void helperDecrementPostCount(User currentUser) {
        int currentPostUser = currentUser.getPostCount();
        currentUser.setPostCount(currentPostUser - 1);
        userRepository.save(currentUser);
    }
}
