package space.space_spring.dao.chat;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.document.ChatMessage;

import java.util.List;

@Repository
public interface ChattingDao extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatRoomId(Long chatRoomId);
}
