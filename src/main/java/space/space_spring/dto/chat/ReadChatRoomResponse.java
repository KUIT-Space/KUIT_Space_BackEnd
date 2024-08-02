package space.space_spring.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ReadChatRoomResponse {
    private List<ChatRoomResponse> chatRoomList;

    public static ReadChatRoomResponse of(List<ChatRoomResponse> chatRooms) {
        return ReadChatRoomResponse.builder()
                .chatRoomList(chatRooms)
                .build();
    }
}
