package myapp.authenticateAPI.service.helpers;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Post;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.dtos.AuthorDTO;
import myapp.authenticateAPI.repository.PostRepository;
import myapp.authenticateAPI.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HelperValidateCreatePost {

    private final PostRepository postRepository;
    private final UserRepository userRepository;


    public Post helperCreateNewPost(Post post, User currentUser) {
        post.setDate(LocalDateTime.now());
        post.setUser(currentUser);
        post.setAuthor(new AuthorDTO(currentUser.getName(), currentUser.getLastName()));

        Post createdPost = postRepository.insert(post);

        currentUser.getPosts().add(createdPost);
        userRepository.save(currentUser);

        return createdPost;
    }

    public URI helperBuildUserUri(Post post) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId()).toUri();
    }
}
