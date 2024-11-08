package space.space_spring.domain.space.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.space.model.entity.Space;

import java.util.Optional;

@Repository
public interface SpaceRepository extends JpaRepository<Space, Long> {

    @Query("SELECT s FROM Space s WHERE s.spaceId = :id AND s.status = 'ACTIVE'")
    Optional<Space> findById(Long id);
}
