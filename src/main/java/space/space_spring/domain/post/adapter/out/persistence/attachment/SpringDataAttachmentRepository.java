package space.space_spring.domain.post.adapter.out.persistence.attachment;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringDataAttachmentRepository extends JpaRepository<AttachmentJpaEntity, Long> {

    @Query("SELECT new space.space_spring.domain.post.adapter.out.persistence.attachment.AttachmentSummary(a.postBase.id, a.attachmentUrl) " +
            "FROM AttachmentJpaEntity a " +
            "WHERE a.postBase.id IN :postIds " +
            "AND a.attachmentType = space.space_spring.domain.post.domain.AttachmentType.IMAGE " +
            "ORDER BY a.createdAt ASC")
    List<AttachmentSummary> findImagesByPostIds(@Param("postIds") List<Long> postIds);
}
