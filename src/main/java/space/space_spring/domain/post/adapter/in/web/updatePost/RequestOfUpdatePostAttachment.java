package space.space_spring.domain.post.adapter.in.web.updatePost;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.domain.AttachmentType;
import space.space_spring.global.common.validation.SelfValidating;
import space.space_spring.global.validator.EnumValidator;

@Getter
@NoArgsConstructor
public class RequestOfUpdatePostAttachment extends SelfValidating<RequestOfUpdatePostAttachment> {

    @EnumValidator(enumClass = AttachmentType.class, message = "첨부파일의 유형은 IMAGE 또는 FILE 이어야 합니다.")
    @NotBlank(message = "첨부파일의 유형은 공백일 수 없습니다.")
    private String valueOfAttachmentType;

    @NotBlank(message = "첨부파일은 공백일 수 없습니다.")
    private MultipartFile attachment;
}
