package space.space_spring.domain.post.adapter.out.persistence.postBase;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.Optional;

public interface SpringDataPostBaseRepository extends JpaRepository<PostBaseJpaEntity, Long> {
    Optional<PostBaseJpaEntity> findByIdAndStatus(Long id, BaseStatusType baseStatusType);
}
