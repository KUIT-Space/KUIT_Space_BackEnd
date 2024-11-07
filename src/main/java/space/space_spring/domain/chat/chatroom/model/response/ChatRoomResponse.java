package space.space_spring.domain.chat.chatroom.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.chat.chatroom.model.ChatRoom;

import java.util.HashMap;

@Builder
@Getter
@AllArgsConstructor
public class ChatRoomResponse {
    private Long id;

    private String name;

    private String imgUrl;

    private HashMap<String, String> lastMsg;

    private String lastTime;

    private int unreadMsgCount;

    public static ChatRoomResponse of(ChatRoom chatRoom, HashMap<String, String> lastMsg, String lastTime, int unreadMsgCount) {
        return ChatRoomResponse.builder()
                .id(chatRoom.getId())
                .name(chatRoom.getName())
                .imgUrl(chatRoom.getImg())
                .lastMsg(lastMsg)
                .lastTime(lastTime)
                .unreadMsgCount(unreadMsgCount)
                .build();
    }
}
