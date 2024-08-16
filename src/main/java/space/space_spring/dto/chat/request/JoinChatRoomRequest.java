package space.space_spring.dto.chat.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class JoinChatRoomRequest {

    @NotEmpty(message = "1명 이상의 멤버를 초대해야 합니다.")
    private List<Long> memberList;
}
