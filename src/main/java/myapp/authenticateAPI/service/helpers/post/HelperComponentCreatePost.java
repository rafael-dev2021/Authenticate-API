package myapp.authenticateAPI.service.helpers.post;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Category;
import myapp.authenticateAPI.domain.entities.Post;
import myapp.authenticateAPI.domain.entities.Tag;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.dtos.AuthorDTO;
import myapp.authenticateAPI.infrastructure.exceptions.CategoryNotFoundException;
import myapp.authenticateAPI.infrastructure.exceptions.TagNotFoundException;
import myapp.authenticateAPI.repository.CategoryRepository;
import myapp.authenticateAPI.repository.PostRepository;
import myapp.authenticateAPI.repository.TagRepository;
import myapp.authenticateAPI.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HelperComponentCreatePost {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;


    public Post helperCreateNewPost(Post post, User currentUser) {
        post.setCreatedAt(LocalDateTime.now());

        AuthorDTO authorDTO = new AuthorDTO(currentUser.getName(), currentUser.getLastName());
        post.setAuthorDTO(authorDTO);

        post.setUser(currentUser);

        List<Category> categories  = loadCategories(post.getCategories());
        List<Tag> tags = loadTags(post.getTags());

        post.setCategories(categories);
        post.setTags(tags);

        Post createdPost = postRepository.insert(post);

        currentUser.getPosts().add(createdPost);
        userRepository.save(currentUser);

        return createdPost;
    }

    public URI helperBuildUserUri(Post post) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId()).toUri();
    }

    private List<Category> loadCategories(List<Category> categories){
        List<Category> loadedCategories = new ArrayList<>();
        for(Category category : categories){
            Category loadedCategory =categoryRepository.findById(category.getId())
                    .orElseThrow(() -> new CategoryNotFoundException(category.getId()));
            loadedCategories.add(loadedCategory);
        }
        return loadedCategories;
    }

    private List<Tag> loadTags(List<Tag> tags){
        List<Tag> loadedTags = new ArrayList<>();
        for(Tag tag : tags){
            Tag loadedTag =tagRepository.findById(tag.getId())
                    .orElseThrow(() -> new TagNotFoundException(tag.getId()));
            loadedTags.add(loadedTag);
        }
        return loadedTags;
    }
}
