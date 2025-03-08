package space.space_spring.domain.post.adapter.in.web.updateComment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestOfPreviousAttachment {

    @NotBlank(message = "삭제할 기존 첨부파일의 id값은 공백일 수 없습니다.")
    private Long attachmentId;      // 수정할 첨부파일의 id 값

    @NotBlank(message = "삭제할 기존 첨부파일의 url 값은 공백일 수 없습니다.")
    private String attachmentUrl;
}
