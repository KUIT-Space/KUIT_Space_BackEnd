package space.space_spring.domain.chat.chatting.model.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatMessageLogResponse {

    private List<ChatMessageResponse> chatMessageLog;

    @Builder(access = AccessLevel.PRIVATE)
    private ChatMessageLogResponse(List<ChatMessageResponse> chatMessageLog) {
        this.chatMessageLog = chatMessageLog;
    }

    public static ChatMessageLogResponse of(List<ChatMessageResponse> chatMessageList) {
        return ChatMessageLogResponse.builder()
                .chatMessageLog(chatMessageList)
                .build();
    }
}
