package myapp.authenticateAPI.repository;

import myapp.authenticateAPI.domain.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends MongoRepository<User, String> {
    UserDetails findByEmail(String email);
}
