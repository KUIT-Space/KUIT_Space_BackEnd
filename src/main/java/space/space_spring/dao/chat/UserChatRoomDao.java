package space.space_spring.dao.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import space.space_spring.entity.UserChatRoom;


public interface UserChatRoomDao extends JpaRepository<UserChatRoom, Long> {
}
