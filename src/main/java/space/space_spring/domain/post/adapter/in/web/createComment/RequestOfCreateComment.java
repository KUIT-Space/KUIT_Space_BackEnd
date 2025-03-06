package space.space_spring.domain.post.adapter.in.web.createComment;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RequestOfCreateComment {

    @NotBlank(message = "댓글 작성 내용은 공백일 수 없습니다.")
    private String content;     // 작성할 댓글 내용

    @NotBlank(message = "댓글의 익명/비익명 여부는 공백일 수 없습니다.")
    private boolean isAnonymous;        // 작성할 댓글의 익명/비익명 여부
}