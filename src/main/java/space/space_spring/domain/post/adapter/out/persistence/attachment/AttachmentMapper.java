package space.space_spring.domain.post.adapter.out.persistence.attachment;

import org.springframework.stereotype.Component;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.domain.Attachment;

@Component
public class AttachmentMapper {

    public AttachmentJpaEntity toJpaEntity(PostBaseJpaEntity postBaseJpaEntity, Attachment domain) {
        return AttachmentJpaEntity.create(
                postBaseJpaEntity,
                domain.getAttachmentType(),
                domain.getAttachmentUrl()
        );
    }
}
