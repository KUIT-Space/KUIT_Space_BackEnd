package space.space_spring.dao.chat;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import space.space_spring.entity.document.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChattingRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatRoomId(Long chatRoomId);

    ChatMessage findTopByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    int countByChatRoomIdAndCreatedAtBetween(Long chatRoomId, LocalDateTime lastReadTime, LocalDateTime lastUpdateTime);
}
