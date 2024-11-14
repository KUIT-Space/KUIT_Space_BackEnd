package space.space_spring.domain.chat.chatroom.model.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReadChatRoomResponse {

    private List<ChatRoomResponse> chatRoomList;

    @Builder
    private ReadChatRoomResponse(List<ChatRoomResponse> chatRoomList) {
        this.chatRoomList = chatRoomList;
    }

    public static ReadChatRoomResponse of(List<ChatRoomResponse> chatRooms) {
        return ReadChatRoomResponse.builder()
                .chatRoomList(chatRooms)
                .build();
    }
}
