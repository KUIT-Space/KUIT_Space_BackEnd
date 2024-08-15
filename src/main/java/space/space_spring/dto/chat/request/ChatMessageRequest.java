package space.space_spring.dto.chat.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.entity.enumStatus.ChatMessageType;

import java.util.HashMap;

@Getter
@NoArgsConstructor
public class ChatMessageRequest {

    @Size(min=1, message = "메시지 내용은 공백일 수 없습니다.")
    private HashMap<String, String> content;

    @NotBlank(message = "스페이스 아이디는 공백일 수 없습니다.")
    private Long spaceId;

    @NotNull(message = "메시지 타입은 공백일 수 없습니다.")
    private ChatMessageType messageType;
}
