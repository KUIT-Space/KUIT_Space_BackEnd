package space.space_spring.domain.chat.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.chat.chatroom.repository.custom.ChatRoomRepositoryCustom;
import space.space_spring.domain.chat.chatroom.model.ChatRoom;
import space.space_spring.global.common.enumStatus.BaseStatusType;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    ChatRoom findByIdAndStatus(Long chatRoomId, BaseStatusType status);
}
