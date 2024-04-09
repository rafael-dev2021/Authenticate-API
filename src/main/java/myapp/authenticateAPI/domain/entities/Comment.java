package myapp.authenticateAPI.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import myapp.authenticateAPI.dtos.AuthorDTO;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class Comment implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String text;
    private LocalDateTime date;
    @JsonIgnore
    private User user;

    private AuthorDTO author;

    public Comment(String text, LocalDateTime date, User user) {
        this.text = text;
        this.date = date;
        this.author = new AuthorDTO(user.getName(), user.getLastName());
    }
}
