package space.space_spring.domain.chat.chatroom.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatSuccessResponse {
    private boolean isSuccess;

    public static ChatSuccessResponse of(boolean isSuccess) {
        return ChatSuccessResponse.builder()
                .isSuccess(isSuccess)
                .build();
    }
}
