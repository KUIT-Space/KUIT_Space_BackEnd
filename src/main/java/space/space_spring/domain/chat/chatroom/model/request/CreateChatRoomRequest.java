package space.space_spring.domain.chat.chatroom.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Builder
public class CreateChatRoomRequest {

    @Size(min = 2, max = 15, message = "채팅방 이름은 2자 이상, 15자 이내의 문자열이어야 합니다.")
    @NotBlank(message = "채팅방 이름은 공백일 수 없습니다.")
    private String name;

    @NotNull(message = "채팅방 이미지는 공백일 수 없습니다.")
    private MultipartFile img;

    @NotEmpty(message = "1명 이상의 멤버를 초대해야 합니다.")
    private List<Long> memberList;
}
