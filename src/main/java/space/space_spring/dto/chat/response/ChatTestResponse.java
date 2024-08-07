package space.space_spring.dto.chat.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ChatTestResponse {
    private String msg;

    public static ChatTestResponse of(String msg) {
        return ChatTestResponse.builder()
                .msg(msg)
                .build();
    }
}
