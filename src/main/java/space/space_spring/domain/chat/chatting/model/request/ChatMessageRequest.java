package space.space_spring.domain.chat.chatting.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import space.space_spring.global.common.enumStatus.ChatMessageType;

import java.util.HashMap;

@Getter
public class ChatMessageRequest {

    @Size(min=1, message = "메시지 내용은 공백일 수 없습니다.")
    private HashMap<String, String> content;

    @NotBlank(message = "스페이스 아이디는 공백일 수 없습니다.")
    private Long spaceId;

    @NotNull(message = "메시지 타입은 공백일 수 없습니다.")
    private ChatMessageType messageType;
}
