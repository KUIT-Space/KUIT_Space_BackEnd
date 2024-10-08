package space.space_spring.dao.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.space_spring.dao.chat.custom.ChatRoomDaoCustom;
import space.space_spring.entity.ChatRoom;

@Repository
public interface ChatRoomDao extends JpaRepository<ChatRoom, Long>, ChatRoomDaoCustom {
    ChatRoom findByIdAndStatus(Long chatRoomId, String status);
}
