package space.space_spring.domain.pay.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.pay.model.PayCreateTargetInfo;

@Getter
@NoArgsConstructor
public class TargetInfoRequest {

    /**
     * PayRequestTarget 엔티티 생성 시 필요한 정보
     * <targetUserId requestedAmount> 쌍
     */

    @NotNull(message = "정산 요청 타겟 유저의 id값은 공백일 수 없습니다.")
    private Long targetUserId;

    @NotNull(message = "정산 요청 금액은 공백일 수 없습니다.")
    @Positive(message = "정산 요청 금액은 양수이어야 합니다.")
    private int requestedAmount;


    @Builder
    private TargetInfoRequest(Long targetUserId, int requestedAmount) {
        this.targetUserId = targetUserId;
        this.requestedAmount = requestedAmount;
    }

    public PayCreateTargetInfo toPayCreateTargetInfo() {
        return PayCreateTargetInfo.builder()
                .targetUserId(targetUserId)
                .requestedAmount(requestedAmount)
                .build();
    }
}
