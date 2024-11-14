package space.space_spring.domain.chat.chatroom.model.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateChatRoomResponse {
    private Long chatRoomId;

    @Builder
    private CreateChatRoomResponse(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public static CreateChatRoomResponse of(Long id) {
        return CreateChatRoomResponse.builder()
                .chatRoomId(id)
                .build();
    }
}
