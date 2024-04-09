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

    @NotBlank(message = "Body is required.")
    @Size(min = 5, max = 30, message = "Body must be between 5 and 30 characters.")
    private String body;
    private LocalDateTime date;

    @DBRef(lazy = true)
    @JsonIgnore
    private User user;

    private AuthorDTO author;

    public Post(String title, String body, LocalDateTime date, User user) {
        this.title = title;
        this.body = body;
        this.date = date;
        this.user = user;
        this.author = new AuthorDTO(user.getName(), user.getLastName());
    }

    private List<Comment> comments = new ArrayList<>();

    public Post(String title, String body, LocalDateTime date) {
        this.title = title;
        this.body = body;
        this.date = date;
    }
}
