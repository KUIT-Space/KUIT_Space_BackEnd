package space.space_spring.domain.pay.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PayRequestInfoDto {

    private Long payRequestId;

    private int totalAmount;            // 정산 총 금액

    private int receiveAmount;          // 현재까지 받은 금액

    private int totalTargetNum;         // 정산 요청한 사람 수

    private int paySendTargetNum;       // 그 중, 돈 보낸 사람 수
}
