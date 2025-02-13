package space.space_spring.domain.post.adapter.in.web.createPost;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.post.domain.AttachmentType;
import space.space_spring.global.common.validation.SelfValidating;
import space.space_spring.global.validator.EnumValidator;

@Getter
@NoArgsConstructor
public class AttachmentOfCreate extends SelfValidating<AttachmentOfCreate> {

    @EnumValidator(enumClass = AttachmentType.class, message = "attachmentType은 IMAGE 또는 FILE 이어야 합니다.")
    private String valueOfAttachmentType;

    @NotBlank(message = "attachmentUrl은 필수입니다.")
    private String attachmentUrl;

    public AttachmentOfCreate(String valueOfAttachmentType, String attachmentUrl) {
        this.valueOfAttachmentType = valueOfAttachmentType;
        this.attachmentUrl = attachmentUrl;
    }
}
