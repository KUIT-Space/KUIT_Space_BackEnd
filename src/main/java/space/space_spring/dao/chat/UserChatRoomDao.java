package space.space_spring.dao.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.entity.ChatRoom;
import space.space_spring.entity.User;
import space.space_spring.entity.UserChatRoom;
import space.space_spring.entity.enumStatus.BaseStatusType;

import java.util.List;


public interface UserChatRoomDao extends JpaRepository<UserChatRoom, Long> {
    UserChatRoom findByUserAndChatRoomAndStatus(User userByUserId, ChatRoom chatRoomByChatRoomId, BaseStatusType status);

    List<UserChatRoom> findByChatRoomAndStatus(ChatRoom chatRoom, BaseStatusType status);
}
