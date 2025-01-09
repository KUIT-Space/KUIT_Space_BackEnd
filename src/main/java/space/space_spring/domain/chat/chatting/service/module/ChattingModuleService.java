package space.space_spring.domain.chat.chatting.service.module;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.chat.chatroom.model.ChatRoom;
import space.space_spring.domain.chat.chatting.model.document.ChatMessage;
import space.space_spring.domain.chat.chatting.repository.ChattingRepository;
import space.space_spring.entity.enumStatus.BaseStatusType;
import space.space_spring.global.util.TimeUtils;

@Component
@RequiredArgsConstructor
public class ChattingModuleService {

    private final ChattingRepository chattingRepository;
    private final TimeUtils timeUtils;

    @Transactional
    public ChatMessage findMostRecentMessage(ChatRoom chatRoom) {
        return Optional.ofNullable(
                        this.chattingRepository.findTopByChatRoomIdAndStatusOrderByCreatedAtDesc(chatRoom.getId(),
                                BaseStatusType.ACTIVE))
                .orElseGet(() -> ChatMessage.builder()
                        .createdAt(timeUtils.getEncodedTime(chatRoom.getCreatedAt()))
                        .content(new HashMap<>(Map.of("text", "메시지를 전송해보세요")))
                        .build());
    }

    @Transactional
    public List<ChatMessage> findChatRooms(Long chatRoomId) {
        return this.chattingRepository.findByChatRoomIdAndStatus(chatRoomId, BaseStatusType.ACTIVE);
    }

    @Transactional
    public ChatMessage save(ChatMessage chatMessage) {
        return this.chattingRepository.save(chatMessage);
    }

    @Transactional
    public int countUnreadMessages(Long chatRoomId, LocalDateTime lastReadTime, LocalDateTime lastUpdateTime) {
        return this.chattingRepository.countByChatRoomIdAndStatusAndCreatedAtBetween(
                chatRoomId,
                BaseStatusType.ACTIVE,
                lastReadTime,
                lastUpdateTime);
    }

}
