package space.space_spring.dto.chat.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import space.space_spring.entity.document.ChatMessage;
import space.space_spring.entity.enumStatus.ChatMessageType;

import java.util.HashMap;
import java.util.Locale;

import static java.time.format.DateTimeFormatter.ofPattern;

@Builder
@Getter
@AllArgsConstructor
public class ChatMessageResponse {

    private HashMap<String, String> content;

    private String createdAt;

    private ChatMessageType messageType;

    private Long senderId;

    private String senderName;

    private String senderImg;

    public static ChatMessageResponse of(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt()
                        .format(ofPattern("yyyy년 MM월 dd일 a h시 m분", new Locale("ko", "KR"))))
                .messageType(chatMessage.getMessageType())
                .senderId(chatMessage.getSenderId())
                .senderName(chatMessage.getSenderName())
                .senderImg(chatMessage.getSenderImg())
                .build();
    }

}
