package space.space_spring.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import space.space_spring.entity.ChatRoom;

@Builder
@Getter
@AllArgsConstructor
public class ChatRoomResponse {
    private Long id;

    private String name;

    private String imgUrl;

    private String lastMsg;

    private String lastTime;

    private int unreadMsgCount;

    public static ChatRoomResponse of(ChatRoom chatRoom, String lastMsg, String lastTime, int unreadMsgCount) {
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
