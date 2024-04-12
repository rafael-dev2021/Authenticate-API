package myapp.authenticateAPI.repository;

import myapp.authenticateAPI.domain.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    UserDetails findByEmail(String email);
    List<User> findByIsActiveTrue();

    List<User> findByIsActiveFalse();
}
