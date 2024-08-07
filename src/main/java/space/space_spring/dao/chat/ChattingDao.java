package space.space_spring.dao.chat;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.document.ChatMessage;

@Repository
public interface ChattingDao extends MongoRepository<ChatMessage, String> {
}
