package space.space_spring.domain.authorization.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.entity.TokenStorage;
import space.space_spring.entity.User;

import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<TokenStorage, Long> {

    Optional<TokenStorage> findByUser(User user);

    @Transactional
    @Modifying
    @Query("DELETE FROM TokenStorage t WHERE t.user = :user")
    void deleteByUser(@Param("user") User user);

    @Transactional
    default void deleteAndFlushByUser(User user) {
        deleteByUser(user);
        flush();  // JPA가 즉시 반영하도록 강제 플러시
    }
}
