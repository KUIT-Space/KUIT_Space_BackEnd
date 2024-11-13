package space.space_spring.domain.chat.chatting.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.chat.chatting.model.document.ChatMessage;
import space.space_spring.entity.enumStatus.ChatMessageType;

import java.util.HashMap;


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
                .createdAt(String.valueOf(chatMessage.getCreatedAt()))
                .messageType(chatMessage.getMessageType())
                .senderId(chatMessage.getSenderId())
                .senderName(chatMessage.getSenderName())
                .senderImg(chatMessage.getSenderImg())
                .build();
    }

}
