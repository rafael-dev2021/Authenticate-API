package myapp.authenticateAPI.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@Document
public class User implements UserDetails {

    @Id
    private String id;
    @NotBlank(message = "Name is required.")
    @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters.")
    private String name;

    @NotBlank(message = "Last name is required.")
    @Size(min = 5, max = 30, message = "Last name must be between 5 and 30 characters.")
    private String lastName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    @Size(min = 10, max = 30, message = "Email must be between 10 and 30 characters.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+).{10,30}$",
            message = "Password must be strong and contain at least one uppercase letter, one lowercase letter, one digit, and one special character.")
    @Size(min = 10, max = 30, message = "Password must be between 10 and 30 characters.")
    private String password;

    private boolean isActive = true;
    private UserRole role = UserRole.USER;

    @DBRef(lazy = true)
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();


    public User(String name, String lastName, String email, String password, boolean isActive, UserRole role) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.role = role;
    }

    public User(String name, String lastName, String email, String password) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
