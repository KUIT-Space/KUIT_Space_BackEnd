package space.space_spring.domain.post.adapter.in.web.updateComment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.domain.post.domain.AttachmentType;
import space.space_spring.global.validator.EnumValidator;

@Getter
@NoArgsConstructor
public class RequestOfUpdateAttachment {

    @EnumValidator(enumClass = AttachmentType.class, message = "첨부파일의 유형은 IMAGE 또는 FILE 이어야 합니다.")
    @NotBlank(message = "첨부파일의 유형은 공백일 수 없습니다.")
    private String valueOfAttachmentType;

    @NotNull(message = "첨부파일은 공백일 수 없습니다.")
    private MultipartFile attachment;
}
