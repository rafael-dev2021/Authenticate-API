package myapp.authenticateAPI.infrastructure.config;

import lombok.RequiredArgsConstructor;
import myapp.authenticateAPI.domain.entities.*;
import myapp.authenticateAPI.repository.CategoryRepository;
import myapp.authenticateAPI.repository.PostRepository;
import myapp.authenticateAPI.repository.TagRepository;
import myapp.authenticateAPI.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class Instantiation implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        // Limpar todos os dados dos repositórios
        userRepository.deleteAll();
        postRepository.deleteAll();
        tagRepository.deleteAll();
        categoryRepository.deleteAll();

        // Criar um usuário admin
        String encryptedPassword = passwordEncoder.encode("@Visual23k+");
        User admin = new User(
                "Rafael",
                "Silva",
                "40028922",
                "Bio",
                "admin@localhost.com",
                encryptedPassword,
                true,
                UserRole.ADMIN);
        userRepository.save(admin);

        // Criar e salvar uma categoria
        Category category1 = new Category("Technology", "tech");
        categoryRepository.save(category1);

        // Criar e salvar uma tag
        Tag tag1 = new Tag("Java", "java");
        tagRepository.save(tag1);

        // Criar e salvar um post
        LocalDateTime createdAt = LocalDateTime.now();
        Post post1 = new Post(
                "title",
                "summary",
                "body",
                "slug",
                createdAt,
                admin
        );
        post1.getCategories().add(category1);
        post1.getTags().add(tag1);
        postRepository.save(post1);

        // Criar um novo usuário
        User user = new User(
                "User",
                "Test",
                "48665456",
                "Bio",
                "user@localhost.com",
                encryptedPassword,
                true,
                UserRole.USER);
        userRepository.save(user);

        // Criar e salvar uma nova categoria
        Category category2 = new Category("Business", "business");
        categoryRepository.save(category2);

        // Criar e salvar uma nova tag
        Tag tag2 = new Tag("C#", "csharp");
        tagRepository.save(tag2);

        // Criar e salvar um novo post
        Post post2 = new Post(
                "title",
                "summary",
                "body",
                "slug",
                createdAt,
                user
        );
        post2.getCategories().add(category2);
        post2.getTags().add(tag2);
        postRepository.save(post2);

        // Criar e salvar comentários e associá-los aos posts
        Comment comment1 = new Comment("Falando sobre java", createdAt, user);
        Comment comment2 = new Comment("Falando sobre c#", createdAt, admin);

        post1.getComments().add(comment1);
        post2.getComments().add(comment2);
        postRepository.saveAll(Arrays.asList(post1, post2));

        // Atualizar a lista de posts dos usuários
        admin.getPosts().add(post1);
        user.getPosts().add(post2);
        userRepository.saveAll(Arrays.asList(admin, user));
    }
}
