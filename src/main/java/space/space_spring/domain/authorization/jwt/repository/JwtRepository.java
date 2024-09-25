package space.space_spring.domain.authorization.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.TokenStorage;
import space.space_spring.entity.User;

import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<TokenStorage, Long> {

    Optional<TokenStorage> findByUser(User user);
    void deleteByUser(User user);
}
