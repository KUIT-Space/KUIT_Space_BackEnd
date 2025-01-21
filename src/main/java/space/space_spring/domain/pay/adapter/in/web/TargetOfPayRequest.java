package space.space_spring.domain.pay.adapter.in.web;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TargetOfPayRequest {

    /**
     * 입력 유효성 검사 수행 -> not null, 정산 요청액은 양수의 정수여야 함
     */

    private Long targetMemberId;

    private int requestedAmount;
}
