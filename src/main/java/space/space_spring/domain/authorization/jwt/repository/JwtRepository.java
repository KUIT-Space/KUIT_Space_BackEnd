package space.space_spring.domain.authorization.jwt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.entity.RefreshTokenStorage;
import space.space_spring.entity.User;

import java.util.Optional;

@Repository
public interface JwtRepository extends JpaRepository<RefreshTokenStorage, Long> {

    Optional<RefreshTokenStorage> findByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM RefreshTokenStorage t WHERE t.user = :user")
    void deleteByUser(@Param("user") User user);

}
