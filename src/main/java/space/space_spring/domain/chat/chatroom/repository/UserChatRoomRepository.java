package space.space_spring.domain.chat.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.domain.chat.chatroom.model.UserChatRoom;
import space.space_spring.entity.enumStatus.BaseStatusType;

import java.util.List;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {
    UserChatRoom findByUserIdAndChatRoomIdAndStatus(Long userId, Long chatRoomId, BaseStatusType status);

    List<UserChatRoom> findByChatRoomIdAndStatus(Long chatRoomId, BaseStatusType status);
}
