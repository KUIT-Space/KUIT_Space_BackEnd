package space.space_spring.domain.post.adapter.out.persistence.tag;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.post.adapter.out.persistence.tag.custom.TagRepositoryCustom;

public interface TagRepository extends JpaRepository<TagJpaEntity, Long>, TagRepositoryCustom {
}
