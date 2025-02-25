package space.space_spring.domain.post.adapter.out.persistence.attachment;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;

import java.util.List;

public interface SpringDataAttachmentRepository extends JpaRepository<AttachmentJpaEntity, Long> {

    List<AttachmentJpaEntity> findByPostBaseIn(List<PostBaseJpaEntity> postBaseJpaEntities);
}
