package space.space_spring.domain.pay.adapter.in.web.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TargetOfPayRequest {

    /**
     * 입력 유효성 검사 수행
     */

    private Long targetMemberId;

    private int requestedAmount;
}
