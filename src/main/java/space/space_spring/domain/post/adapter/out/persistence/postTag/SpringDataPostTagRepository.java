package space.space_spring.domain.post.adapter.out.persistence.postTag;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.post.domain.PostTag;

public interface SpringDataPostTagRepository extends JpaRepository<PostTagJpaEntity, Long> {
}
