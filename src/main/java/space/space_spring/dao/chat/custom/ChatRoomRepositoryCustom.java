package space.space_spring.dao.chat.custom;

import space.space_spring.entity.ChatRoom;
import space.space_spring.entity.Space;
import space.space_spring.domain.user.model.entity.User;

import java.util.List;

public interface ChatRoomRepositoryCustom {
    List<ChatRoom> findByUserAndSpace(User user, Space space);
}
