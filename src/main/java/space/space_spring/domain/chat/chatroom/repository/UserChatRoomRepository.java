package space.space_spring.domain.chat.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.chat.chatroom.model.ChatRoom;
import space.space_spring.entity.User;
import space.space_spring.domain.chat.chatting.model.UserChatRoom;
import space.space_spring.entity.enumStatus.BaseStatusType;

import java.util.List;


public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {
    UserChatRoom findByUserAndChatRoomAndStatus(User userByUserId, ChatRoom chatRoomByChatRoomId, BaseStatusType status);

    List<UserChatRoom> findByChatRoomAndStatus(ChatRoom chatRoom, BaseStatusType status);
}
