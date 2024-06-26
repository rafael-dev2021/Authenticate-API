package myapp.authenticateAPI.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
public class PostSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(HttpMethod.GET, "/v1/post/posts").permitAll()
                .requestMatchers(HttpMethod.GET, "/v1/post/{id}").permitAll()
                .requestMatchers(HttpMethod.POST, "/v1/post/create").permitAll()
                .requestMatchers(HttpMethod.PUT, "/v1/post/{id}").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/v1/post/{id}").permitAll()
        );
    }
}