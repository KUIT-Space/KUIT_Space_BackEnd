package space.space_spring.domain.pay.adapter.in.web.createPay;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.global.common.validation.SelfValidating;

@Getter
@NoArgsConstructor
public class TargetOfPayRequest extends SelfValidating<TargetOfPayRequest> {

    /**
     * 입력 유효성 검사 수행
     */

    @NotNull(message = "정산 요청 타겟 유저의 id값은 공백일 수 없습니다.")
    private Long targetMemberId;

    @NotNull(message = "정산 요청 금액은 공백일 수 없습니다.")
    @Positive(message = "정산 요청 금액은 양수이어야 합니다.")
    private int requestedAmount;

    public TargetOfPayRequest(Long targetMemberId, int requestedAmount) {
        this.targetMemberId = targetMemberId;
        this.requestedAmount = requestedAmount;
        this.validateSelf();
    }

}
