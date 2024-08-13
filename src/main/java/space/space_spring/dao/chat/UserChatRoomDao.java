package space.space_spring.dao.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.entity.ChatRoom;
import space.space_spring.entity.User;
import space.space_spring.entity.UserChatRoom;

import java.util.List;


public interface UserChatRoomDao extends JpaRepository<UserChatRoom, Long> {
    UserChatRoom findByUserAndChatRoom(User userByUserId, ChatRoom chatRoomByChatRoomId);

    List<UserChatRoom> findByChatRoom(ChatRoom chatRoom);
}
