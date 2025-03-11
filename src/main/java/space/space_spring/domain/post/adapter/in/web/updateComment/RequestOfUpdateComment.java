package space.space_spring.domain.post.adapter.in.web.updateComment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class RequestOfUpdateComment {

    @NotBlank(message = "댓글 작성 내용은 공백일 수 없습니다.")
    private String content;     // 작성할 댓글 내용

    @NotNull(message = "댓글의 익명/비익명 여부는 공백일 수 없습니다.")
    private Boolean isAnonymous;        // 작성할 댓글의 익명/비익명 여부


    /**
     * space 2.0 v1 에서는 댓글 수정 시에 첨부파일 update 요구사항 없음
     */

//    @Nullable
//    @Valid
//    private List<RequestOfUpdateAttachment> newAttachments;        // 수정할 첨부 파일 리스트

}
