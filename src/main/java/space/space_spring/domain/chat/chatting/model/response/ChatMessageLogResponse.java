package space.space_spring.domain.chat.chatting.model.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChatMessageLogResponse {
    private List<ChatMessageResponse> chatMessageLog;

    public static ChatMessageLogResponse of(List<ChatMessageResponse> chatMessageList) {
        return ChatMessageLogResponse.builder()
                .chatMessageLog(chatMessageList)
                .build();
    }
}
