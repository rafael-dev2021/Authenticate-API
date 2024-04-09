package myapp.authenticateAPI.infrastructure.config;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.Comment;
import myapp.authenticateAPI.domain.entities.Post;
import myapp.authenticateAPI.domain.entities.User;
import myapp.authenticateAPI.domain.entities.UserRole;
import myapp.authenticateAPI.repository.PostRepository;
import myapp.authenticateAPI.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class Instantiation implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;

    @Override
    public void run(String... args) {
        userRepository.deleteAll();
        postRepository.deleteAll();
        String encryptedPassword = passwordEncoder.encode("@Visual23k+");
        User admin = new User("Rafael", "Silva", "admin@localhost.com", encryptedPassword, true, UserRole.ADMIN);
        userRepository.saveAll(List.of(admin));

        LocalDateTime date = LocalDateTime.now();

        Post post1 = new Post("Primeiro post", "Testando", date, admin);
        Post post2 = new Post("Segundo post", "Testando", date, admin);

        Comment comment1 = new Comment("Falando do primeiro post", date, admin);
        Comment comment2 = new Comment("Falando do segundo post", date, admin);

        post1.getComments().add(comment1);
        post2.getComments().add(comment2);

        postRepository.saveAll(Arrays.asList(post1, post2));

        admin.getPosts().addAll(Arrays.asList(post1, post2));
        userRepository.save(admin);
    }
}



/*
 @Override
 public void run(String... args) {
        if (userRepository.count() == 0) {
            String encryptedPassword = passwordEncoder.encode("@Visual23k+");
            User admin = new User("Rafael", "Silva", "admin@localhost.com", encryptedPassword, true, UserRole.ADMIN);
            userRepository.save(admin);

            LocalDateTime date = LocalDateTime.now();

            Post post1 = new Post("Primeiro post", "Testando", date, admin);
            Post post2 = new Post("Segundo post", "Testando", date, admin);

            Comment comment1 = new Comment("Falando do primeiro post", date, admin);
            Comment comment2 = new Comment("Falando do segundo post", date, admin);

            post1.getComments().add(comment1);
            post2.getComments().add(comment2);

            postRepository.saveAll(Arrays.asList(post1, post2));

            admin.getPosts().addAll(Arrays.asList(post1, post2));
            userRepository.save(admin);
        }
    }
 */