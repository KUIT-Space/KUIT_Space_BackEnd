package space.space_spring.domain.post.adapter.out.persistence.postTag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.space_spring.domain.post.domain.PostTag;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.List;

public interface SpringDataPostTagRepository extends JpaRepository<PostTagJpaEntity, Long> {

    List<PostTagJpaEntity> findAllByPostBaseIdAndStatus(Long postId, BaseStatusType status);
}
