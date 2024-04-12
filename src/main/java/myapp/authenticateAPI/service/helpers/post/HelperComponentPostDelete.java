package myapp.authenticateAPI.service.helpers.post;

import myapp.authenticateAPI.domain.entities.Post;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.infrastructure.exceptions.DomainAccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class HelperComponentPostDelete {

    public void helperValidatePostDeletion(Post existingPost, User currentUser) {
        if (!existingPost.getUser().getId().equals(currentUser.getId())) {
            throw new DomainAccessDeniedException();
        }
    }
}
