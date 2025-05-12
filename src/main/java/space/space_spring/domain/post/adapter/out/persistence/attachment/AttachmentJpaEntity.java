package space.space_spring.domain.post.adapter.out.persistence.attachment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.domain.AttachmentType;
import space.space_spring.global.common.entity.BaseJpaEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "Attachment")
public class AttachmentJpaEntity extends BaseJpaEntity {

    @Id
    @GeneratedValue
    @NotNull
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_base_id")
    @NotNull
    private PostBaseJpaEntity postBase;

    @Enumerated(EnumType.STRING)
    @Column(name = "attachment_type")
    @NotNull
    private AttachmentType attachmentType;

    @Column(name = "attachment_url")
    @NotNull
    private String attachmentUrl;

    private AttachmentJpaEntity(PostBaseJpaEntity postBase, AttachmentType attachmentType, String attachmentUrl) {
        this.postBase = postBase;
        this.attachmentType = attachmentType;
        this.attachmentUrl = attachmentUrl;
    }

    public static AttachmentJpaEntity create(PostBaseJpaEntity postBase, AttachmentType attachmentType, String attachmentUrl) {
        return new AttachmentJpaEntity(postBase, attachmentType, attachmentUrl);
    }

    public void softDelete() {
        this.updateToInactive();
    }
}
