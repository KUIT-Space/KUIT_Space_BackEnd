package space.space_spring.domain.post.adapter.out.persistence.post;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.post.adapter.out.persistence.post.PostJpaEntity;

public interface SpringDataPostRepository extends JpaRepository<PostJpaEntity, Long> {
}
