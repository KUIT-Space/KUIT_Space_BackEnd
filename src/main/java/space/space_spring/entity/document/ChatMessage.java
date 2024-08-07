package space.space_spring.entity.document;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import space.space_spring.entity.enumStatus.ChatMessageType;

import java.time.LocalDateTime;
import java.util.HashMap;

@Document(collection = "chat_message")
@Getter
@Builder
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

    public static ChatMessage of(HashMap<String, String> content, Long chatRoomId, Long spaceId, Long senderId, String senderName, String senderImg, ChatMessageType messageType, LocalDateTime createdAt) {
        return ChatMessage.builder()
                .content(content)
                .chatRoomId(chatRoomId)
                .spaceId(spaceId)
                .senderId(senderId)
                .senderName(senderName)
                .senderImg(senderImg)
                .messageType(messageType)
                .createdAt(createdAt)
                .build();
    }

}
