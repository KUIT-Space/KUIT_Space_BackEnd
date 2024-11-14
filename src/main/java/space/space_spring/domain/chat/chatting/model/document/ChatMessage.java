package space.space_spring.domain.chat.chatting.model.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import space.space_spring.entity.enumStatus.ChatMessageType;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;

@Document(collection = "chat_message")
@Getter
@TypeAlias("ChatMessage")
public class ChatMessage {
    @Id
    private String id;

    private HashMap<String, String> content;

    private Long chatRoomId;

    private Long spaceId;

    private Long senderId;

    private String senderName;

    private String senderImg;

    private ChatMessageType messageType;

    private LocalDateTime createdAt;

    @Builder
    private ChatMessage(String id, HashMap<String, String> content, Long chatRoomId, Long spaceId, Long senderId,
                       String senderName, String senderImg, ChatMessageType messageType, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.chatRoomId = chatRoomId;
        this.spaceId = spaceId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderImg = senderImg;
        this.messageType = messageType;
        this.createdAt = createdAt;
    }

    public static ChatMessage create(HashMap<String, String> content, Long chatRoomId, Long spaceId, Long senderId, String senderName, String senderImg, ChatMessageType messageType) {
        return ChatMessage.builder()
                .content(content)
                .chatRoomId(chatRoomId)
                .spaceId(spaceId)
                .senderId(senderId)
                .senderName(senderName)
                .senderImg(senderImg)
                .messageType(messageType)
                .createdAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();
    }
}
