package space.space_spring.domain.post.adapter.in.web.createBoard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.global.common.validation.SelfValidating;

@Getter
@NoArgsConstructor
public class RequestOfCreateBoard extends SelfValidating<RequestOfCreateBoard> {

    @NotNull(message = "Discord ID는 필수입니다.")
    private Long discordId;

    @NotBlank(message = "게시판 타입은 필수입니다.")
    private String boardType;

    @NotBlank(message = "게시판 이름은 필수입니다.")
    private String boardName;

    @NotBlank(message = "WebHook URL은 필수입니다.")
    private String webhookUrl;

    public RequestOfCreateBoard(Long discordId, String boardType, String boardName, String webhookUrl) {
        this.discordId = discordId;
        this.boardType = boardType;
        this.boardName = boardName;
        this.webhookUrl = webhookUrl;
        this.validateSelf();
    }

}
