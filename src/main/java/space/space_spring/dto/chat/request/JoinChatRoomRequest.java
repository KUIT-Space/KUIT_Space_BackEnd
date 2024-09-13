package space.space_spring.dto.chat.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor /* controller단 테스트 위해 적용*/
@AllArgsConstructor
public class JoinChatRoomRequest {

    @NotEmpty(message = "1명 이상의 멤버를 초대해야 합니다.")
    private List<Long> memberList;
}
