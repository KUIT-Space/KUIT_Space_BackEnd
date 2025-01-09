package space.space_spring.domain.chat.chatting.model.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.chat.chatting.model.document.ChatMessage;
import space.space_spring.entity.enumStatus.ChatMessageType;

import java.util.HashMap;


@Getter
public class ChatMessageResponse {

    private HashMap<String, String> content;

    private String createdAt;

    private ChatMessageType messageType;

    private Long senderId;

    private String senderName;

    private String senderImg;

    @Builder(access = AccessLevel.PRIVATE)
    private ChatMessageResponse(HashMap<String, String> content, String createdAt, ChatMessageType messageType,
                               Long senderId, String senderName, String senderImg) {
        this.content = content;
        this.createdAt = createdAt;
        this.messageType = messageType;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderImg = senderImg;
    }

    public static ChatMessageResponse create(ChatMessage chatMessage) {
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
