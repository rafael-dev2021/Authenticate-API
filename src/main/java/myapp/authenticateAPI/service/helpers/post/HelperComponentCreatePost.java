package myapp.authenticateAPI.service.helpers.post;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Category;
import myapp.authenticateAPI.domain.entities.Post;
import myapp.authenticateAPI.domain.entities.Tag;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.dtos.AuthorDTO;
import myapp.authenticateAPI.dtos.PostDTO;
import myapp.authenticateAPI.infrastructure.exceptions.CategoryNotFoundException;
import myapp.authenticateAPI.infrastructure.exceptions.TagNotFoundException;
import myapp.authenticateAPI.repository.*;
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
    //private final CommentRepository commentRepository;


    public Post helperCreateNewPost(PostDTO postDTO, User currentUser) {
        Post post = new Post();
        post.setTitle(postDTO.title());
        post.setSummary(postDTO.summary());
        post.setBody(postDTO.body());
        post.setSlug(postDTO.slug());
        post.setCreatedAt(LocalDateTime.now());

        AuthorDTO authorDTO = new AuthorDTO(currentUser.getName(), currentUser.getLastName());
        post.setAuthorDTO(authorDTO);

        post.setUser(currentUser);

        List<Category> categories = loadCategories(postDTO.categories());
        List<Tag> tags = loadTags(postDTO.tags());
        //     List<Comment> comments = loadComments(postDTO.comments());

        post.setCategories(categories);
        post.setTags(tags);
        //   post.setComments(comments);

        Post createdPost = postRepository.save(post);

        for (Category category : categories) {
            incrementCategoryPostCount(category.getId(), createdPost);
        }

        currentUser.getPosts().add(createdPost);
        userRepository.save(currentUser);

        return createdPost;
    }


    public URI helperBuildUserUri(Post post) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(post.getId()).toUri();
    }
    public void incrementCategoryPostCount(String categoryId, Post post) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        category.setPostCount(category.getPostCount() + 1);
        category.getPosts().add(post);
        categoryRepository.save(category);
    }

    public void decrementCategoryPostCount(String categoryId, String postId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        category.setPostCount(category.getPostCount() - 1);
        category.getPosts().removeIf(post -> post.getId().equals(postId));
        categoryRepository.save(category);
    }


    private List<Category> loadCategories(List<Category> categories) {
        List<Category> loadedCategories = new ArrayList<>();
        for (Category category : categories) {
            Category loadedCategory = categoryRepository.findById(category.getId())
                    .orElseThrow(() -> new CategoryNotFoundException(category.getId()));
            loadedCategories.add(loadedCategory);
        }
        return loadedCategories;
    }

    private List<Tag> loadTags(List<Tag> tags) {
        List<Tag> loadedTags = new ArrayList<>();
        for (Tag tag : tags) {
            Tag loadedTag = tagRepository.findById(tag.getId())
                    .orElseThrow(() -> new TagNotFoundException(tag.getId()));
            loadedTags.add(loadedTag);
        }
        return loadedTags;
    }

    //   private List<Comment> loadComments(List<Comment> commentList) {
    //        List<Comment> loadedComments = new ArrayList<>();
    //        for(Comment comment : commentList){
    //            Comment loadedComment = commentRepository.findById(comment.getId())
    //                    .orElseThrow(() -> new TagNotFoundException(comment.getId()));
    //            loadedComments.add(loadedComment);
    //        }
    //        return loadedComments;
    //    }
}
