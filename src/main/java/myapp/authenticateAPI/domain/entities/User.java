package myapp.authenticateAPI.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
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

    private String name;

    private String lastName;

    private String phoneNumber;

    private String bio;

    private String email;

    private String password;

    private UserRole role;

    private boolean isActive;
    private int failedLoginAttempts = 0;
    private LocalDateTime lockExpirationTime;

    @DBRef(lazy = true)
    @JsonIgnore
    private List<Post> posts = new ArrayList<>();

    @JsonIgnore
    @Transient
    private int postCount;


    public User(String name, String lastName, String phoneNumber, String bio, String email, String password, boolean isActive, UserRole role) {
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.email = email;
        this.password = password;
        this.isActive = isActive;
        this.role = role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN) return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
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
        return lockExpirationTime == null || lockExpirationTime.isBefore(LocalDateTime.now());
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public void lockAccountForHours() {
        lockExpirationTime = LocalDateTime.now().plusHours(2);
        isActive = false;
    }
}
