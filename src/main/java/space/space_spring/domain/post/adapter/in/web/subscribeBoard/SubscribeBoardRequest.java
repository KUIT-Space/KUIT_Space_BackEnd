package space.space_spring.domain.post.adapter.in.web.subscribeBoard;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.global.common.validation.SelfValidating;

@Getter
@NoArgsConstructor
public class SubscribeBoardRequest extends SelfValidating<SubscribeBoardRequest> {

    @NotNull(message = "게시판 아이디는 필수입니다.")
    private Long boardId;

    @Nullable
    private Long tagId;

    public SubscribeBoardRequest(Long boardId, Long tagId) {
        this.boardId = boardId;
        this.tagId = tagId;
    }
}
