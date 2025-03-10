package space.space_spring.domain.post.adapter.out.persistence.attachment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import space.space_spring.domain.post.domain.AttachmentType;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.List;

public interface SpringDataAttachmentRepository extends JpaRepository<AttachmentJpaEntity, Long> {

    @Query("SELECT new space.space_spring.domain.post.adapter.out.persistence.attachment.AttachmentSummary(a.postBase.id, a.attachmentUrl) " +
            "FROM AttachmentJpaEntity a " +
            "WHERE a.postBase.id IN :postIds " +
            "AND a.attachmentType = :type " +
            "ORDER BY a.createdAt ASC")
    List<AttachmentSummary> findImagesByPostIds(@Param("postIds") List<Long> postIds,
                                                @Param("type")AttachmentType type);

    @Query("SELECT a FROM AttachmentJpaEntity a WHERE a.status = 'ACTIVE' AND a.attachmentUrl IN :attachmentUrls")
    List<AttachmentJpaEntity> findByAttachmentUrlInAndStatus(List<String> attachmentUrls);

    List<AttachmentJpaEntity> findByIdAndStatus(Long postId, BaseStatusType type);
}
