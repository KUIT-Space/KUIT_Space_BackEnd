package space.space_spring.domain.post.adapter.out.persistence.postBase;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;

public interface SpringDataPostBaseRepository extends JpaRepository<PostBaseJpaEntity, Long> {
}
