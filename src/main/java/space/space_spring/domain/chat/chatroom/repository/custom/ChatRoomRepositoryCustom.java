package space.space_spring.domain.chat.chatroom.repository.custom;

import space.space_spring.domain.chat.chatroom.model.ChatRoom;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoom> findByUserAndSpace(User user, Space space);
}
