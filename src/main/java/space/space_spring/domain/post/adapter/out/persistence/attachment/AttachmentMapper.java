package space.space_spring.domain.post.adapter.out.persistence.attachment;

import org.springframework.stereotype.Component;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.domain.Attachment;
import space.space_spring.global.common.entity.BaseInfo;

@Component
public class AttachmentMapper {

    public AttachmentJpaEntity toJpaEntity(PostBaseJpaEntity postBaseJpaEntity, Attachment domain) {
        return AttachmentJpaEntity.create(
                postBaseJpaEntity,
                domain.getAttachmentType(),
                domain.getAttachmentUrl()
        );
    }

    public Attachment toDomainEntity(AttachmentJpaEntity jpaEntity) {
        BaseInfo baseInfo = BaseInfo.of(
                jpaEntity.getPostBase().getCreatedAt(),
                jpaEntity.getPostBase().getLastModifiedAt(),
                jpaEntity.getPostBase().getStatus()
        );
        return Attachment.of(
                jpaEntity.getId(),
                jpaEntity.getPostBase().getId(),
                jpaEntity.getAttachmentType(),
                jpaEntity.getAttachmentUrl(),
                baseInfo
        );
    }
}
