package space.space_spring.domain.chat.chatting.model;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import space.space_spring.domain.chat.chatting.model.document.ChatMessage;
import space.space_spring.domain.chat.chatting.model.response.ChatMessageResponse;

public class ChatMessages {

    private final List<ChatMessage> chatMessages;

    private ChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = Collections.unmodifiableList(chatMessages);
    }

    public static ChatMessages of(List<ChatMessage> chatMessages) {
        return new ChatMessages(chatMessages);
    }

    public List<ChatMessageResponse> toChatMessageResponses() {
        return chatMessages.stream()
                .map(ChatMessageResponse::create)
                .collect(Collectors.toList());
    }
}
