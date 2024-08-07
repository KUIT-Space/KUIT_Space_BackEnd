package space.space_spring.dao.chat.custom;

import space.space_spring.entity.ChatRoom;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;

import java.util.List;

public interface ChatRoomDaoCustom {
    List<ChatRoom> findByUserAndSpace(User user, Space space);
}
