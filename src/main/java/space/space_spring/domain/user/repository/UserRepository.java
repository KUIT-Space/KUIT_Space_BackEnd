package space.space_spring.domain.user.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.user.model.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.status = 'ACTIVE'")
    Optional<User> findById(Long userId);
}
