package space.space_spring.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CreateChatRoomResponse {
    private Long id;

    public static CreateChatRoomResponse of(Long id) {
        return CreateChatRoomResponse.builder()
                .id(id)
                .build();
    }
}
