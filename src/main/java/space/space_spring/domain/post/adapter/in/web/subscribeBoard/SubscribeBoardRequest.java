package space.space_spring.domain.post.adapter.in.web.subscribeBoard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.global.common.validation.SelfValidating;

@Getter
@NoArgsConstructor
public class SubscribeBoardRequest extends SelfValidating<SubscribeBoardRequest> {

    @NotNull(message = "게시판 아이디는 필수입니다.")
    private Long boardId;

    @NotBlank(message = "태그 이름은 공백일 수 없습니다.")
    private String tagName;

    public SubscribeBoardRequest(Long boardId, String tagName) {
        this.boardId = boardId;
        this.tagName = tagName;
        this.validateSelf();
    }
}
