package space.space_spring.domain.chat.chatting.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.chat.chatting.model.document.ChatMessage;

import java.time.LocalDateTime;
import java.util.List;
import space.space_spring.entity.enumStatus.BaseStatusType;

@Repository
public interface ChattingRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatRoomIdAndStatus(Long chatRoomId, BaseStatusType status);

    ChatMessage findTopByChatRoomIdAndStatusOrderByCreatedAtDesc(Long chatRoomId, BaseStatusType status);

    int countByChatRoomIdAndStatusAndCreatedAtBetween(Long chatRoomId, BaseStatusType status, LocalDateTime lastReadTime, LocalDateTime lastUpdateTime);
}
