package space.space_spring.domain.chat.chatroom.model.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreateChatRoomResponse {
    private Long chatRoomId;

    @Builder(access = AccessLevel.PRIVATE)
    private CreateChatRoomResponse(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public static CreateChatRoomResponse of(Long id) {
        return CreateChatRoomResponse.builder()
                .chatRoomId(id)
                .build();
    }
}
