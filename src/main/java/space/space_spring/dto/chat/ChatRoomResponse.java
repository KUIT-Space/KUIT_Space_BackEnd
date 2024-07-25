package space.space_spring.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

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

    public static ChatRoomResponse of(Long id, String name, String imgUrl, String lastMsg, String lastTime, int unreadMsgCount) {
        return ChatRoomResponse.builder()
                .id(id)
                .name(name)
                .imgUrl(imgUrl)
                .lastMsg(lastMsg)
                .lastTime(lastTime)
                .unreadMsgCount(unreadMsgCount)
                .build();
    }
}
