package space.space_spring.dao.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import space.space_spring.dao.chat.custom.ChatRoomDaoCustom;
import space.space_spring.entity.ChatRoom;
import space.space_spring.entity.Space;

import java.util.List;

@Repository
public interface ChatRoomDao extends JpaRepository<ChatRoom, Long>, ChatRoomDaoCustom {
    List<ChatRoom> findBySpace(Space space);
}
