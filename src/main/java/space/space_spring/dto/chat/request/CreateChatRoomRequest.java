package space.space_spring.dto.chat.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateChatRoomRequest {

    @Length(min = 2, max = 15, message = "채팅방 이름은 2자 이상, 15자 이내의 문자열이어야 합니다.")
    @NotBlank(message = "채팅방 이름은 공백일 수 없습니다.")
    private String name;

    private MultipartFile img;

    // TODO: member 조회 API 개발 시 수정 예정
    private List<String> memberList;
}
