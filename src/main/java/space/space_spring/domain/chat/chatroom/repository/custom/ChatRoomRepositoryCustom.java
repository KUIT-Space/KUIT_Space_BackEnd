package space.space_spring.domain.chat.chatroom.repository.custom;

import space.space_spring.domain.chat.chatroom.model.ChatRoom;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoom> findByUserIdAndSpaceId(Long userId, Long spaceId);
}
