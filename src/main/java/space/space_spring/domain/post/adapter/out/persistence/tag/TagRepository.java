package space.space_spring.domain.post.adapter.out.persistence.tag;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.post.adapter.out.persistence.tag.custom.TagRepositoryCustom;
import space.space_spring.global.common.enumStatus.BaseStatusType;

public interface TagRepository extends JpaRepository<TagJpaEntity, Long>, TagRepositoryCustom {
    Optional<TagJpaEntity> findByIdAndStatus(Long tagId, BaseStatusType status);
}
