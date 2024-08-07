package space.space_spring.dto.chat.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.entity.enumStatus.ChatMessageType;

import java.util.HashMap;

@Getter
@NoArgsConstructor
public class ChatMessageRequest {
    @NotBlank(message = "전송자 아이디는 공백일 수 없습니다.")
    private Long senderId;

    @NotBlank(message = "메시지 내용은 공백일 수 없습니다.")
    private HashMap<String, String> content;

    @NotBlank(message = "스페이스 아이디는 공백일 수 없습니다.")
    private Long spaceId;

    @NotBlank(message = "메시지 타입은 공백일 수 없습니다.")
    private ChatMessageType messageType;
}
