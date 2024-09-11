package space.space_spring.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.TokenStorage;
import space.space_spring.entity.User;

@Repository
public interface JwtRepository extends JpaRepository<TokenStorage, Long> {

    TokenStorage findByUser(User user);
}
