package space.space_spring.dao.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.space_spring.dao.chat.custom.ChatRoomRepositoryCustom;
import space.space_spring.entity.ChatRoom;
import space.space_spring.entity.enumStatus.BaseStatusType;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    ChatRoom findByIdAndStatus(Long chatRoomId, BaseStatusType status);
}
