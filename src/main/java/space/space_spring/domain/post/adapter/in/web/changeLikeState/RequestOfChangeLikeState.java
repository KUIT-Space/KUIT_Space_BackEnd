package space.space_spring.domain.post.adapter.in.web.changeLikeState;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RequestOfChangeLikeState {

    @NotNull(message = "좋아요 changeTo는 null 일 수 없습니다.")
    private Boolean changeTo;       // true -> 좋아요 on, false -> 좋아요 off

    public RequestOfChangeLikeState(boolean changeTo) {
        this.changeTo = changeTo;
    }
}
