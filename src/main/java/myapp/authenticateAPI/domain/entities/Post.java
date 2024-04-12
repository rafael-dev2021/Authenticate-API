package myapp.authenticateAPI.domain.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.*;
import myapp.authenticateAPI.dtos.AuthorDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import java.io.*;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Post implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @NotBlank(message = "Title is required.")
    @Size(min = 5, max = 30, message = "Title must be between 5 and 30 characters.")
    private String title;

    private String summary;

    @NotBlank(message = "Body is required.")
    @Size(min = 5, max = 30, message = "Body must be between 5 and 30 characters.")
    private String body;
    private String slug;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @DBRef(lazy = true)
    @JsonIgnore
    private User user;

    private List<Category> categories = new ArrayList<>();
    private List<Tag> tags = new ArrayList<>();

    private AuthorDTO authorDTO;
    private List<Comment> comments = new ArrayList<>();


    public Post(String title, String summary, String body, String slug, LocalDateTime createdAt, User user) {
        this.title = title;
        this.summary = summary;
        this.body = body;
        this.slug = slug;
        this.createdAt = createdAt;
        this.user = user;
        this.authorDTO = new AuthorDTO(user.getName(), user.getLastName());
    }


    public void UpdatedPost(String title, String summary, String body, String slug, LocalDateTime updatedAt) {
        this.title = title;
        this.summary = summary;
        this.body = body;
        this.slug = slug;
        this.updatedAt = updatedAt;
    }
}
