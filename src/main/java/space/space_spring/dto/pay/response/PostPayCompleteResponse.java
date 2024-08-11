package space.space_spring.dto.pay.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostPayCompleteResponse {

    private Long payRequestId;

    private boolean isComplete;             // 유저가 돈 낸 정산의 완료 여부

}
