package space.space_spring.domain.chat.chatroom.model.response;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.chat.chatroom.model.ChatRoom;

import java.util.HashMap;

@Getter
public class ChatRoomResponse {
    private Long id;

    private String name;

    private String imgUrl;

    private HashMap<String, String> lastMsg;

    private String lastTime;

    private int unreadMsgCount;

    @Builder
    private ChatRoomResponse(Long id, String name, String imgUrl, HashMap<String, String> lastMsg, String lastTime,
                            int unreadMsgCount) {
        this.id = id;
        this.name = name;
        this.imgUrl = imgUrl;
        this.lastMsg = lastMsg;
        this.lastTime = lastTime;
        this.unreadMsgCount = unreadMsgCount;
    }

    public static ChatRoomResponse create(ChatRoom chatRoom, HashMap<String, String> lastMsg, String lastTime, int unreadMsgCount) {
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
