package space.space_spring.domain.post.adapter.out.persistence.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;

import java.util.List;

public interface SpringDataPostRepository extends JpaRepository<PostJpaEntity, Long> {

}
