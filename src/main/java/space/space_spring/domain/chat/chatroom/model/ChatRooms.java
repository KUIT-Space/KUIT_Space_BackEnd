package space.space_spring.domain.chat.chatroom.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import space.space_spring.domain.chat.chatroom.model.dto.LastMessageInfoDto;
import space.space_spring.domain.chat.chatroom.model.response.ChatRoomResponse;

public class ChatRooms {

    private List<ChatRoom> chatRooms;

    private ChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public static ChatRooms of(List<ChatRoom> chatRooms) {
        return new ChatRooms(chatRooms);
    }

    public List<ChatRoomResponse> toChatRoomResponses(
            Long userId,
            Function<ChatRoom, LastMessageInfoDto> lastMessageFinder,
            BiFunction<Long, ChatRoom, Integer> unreadMessageCounter) {
        return chatRooms.stream()
                .map(chatRoom -> {
                    LastMessageInfoDto lastMessageInfo = lastMessageFinder.apply(chatRoom);
                    LocalDateTime lastUpdateTime = lastMessageInfo.getLastUpdateTime();
                    HashMap<String, String> lastContent = lastMessageInfo.getLastContent();

                    int unreadMsgCount = unreadMessageCounter.apply(userId, chatRoom);

                    return ChatRoomResponse.create(chatRoom, lastContent, String.valueOf(lastUpdateTime),
                            unreadMsgCount);
                })
                .filter(Objects::nonNull)
                .toList();
    }
}
