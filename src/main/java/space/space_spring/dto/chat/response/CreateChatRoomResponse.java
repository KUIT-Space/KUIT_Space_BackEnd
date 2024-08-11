package space.space_spring.dto.chat.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateChatRoomResponse {
    private Long chatRoomId;

    public static CreateChatRoomResponse of(Long id) {
        return CreateChatRoomResponse.builder()
                .chatRoomId(id)
                .build();
    }
}
