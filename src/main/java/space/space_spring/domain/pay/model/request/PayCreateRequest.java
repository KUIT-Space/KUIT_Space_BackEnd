package space.space_spring.domain.pay.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.pay.model.PayCreateTargetInfo;
import space.space_spring.domain.pay.model.PayType;
import space.space_spring.global.common.EnumValidator;

import java.util.List;

@Getter
@NoArgsConstructor
public class PayCreateRequest {

    /**
     * PayRequest 엔티티 생성 시 필요한 정보
     */
    @NotBlank(message = "총 정산 요청 금액은 공백일 수 없습니다.")
    private int totalAmount;

    @NotBlank(message = "은행 이름은 공백일 수 없습니다.")
    private String bankName;

    @NotBlank(message = "은행 계좌 번호는 공백일 수 없습니다.")
    private String bankAccountNum;

    /**
     * PayRequestTarget 엔티티 생성 시 필요한 정보
     * <targetUserId requestedAmount> 쌍
     */
    private List<PayCreateTargetRequest> targetInfoRequests;

    @EnumValidator(enumClass = PayType.class, message = "payType은 INDIVIDUAL 또는 EQUAL_SPLIT 이어야 합니다.")
    private String payType;

    @Getter
    @NoArgsConstructor
    private static class PayCreateTargetRequest {

        @NotBlank(message = "정산 요청 타겟 유저의 id값은 공백일 수 없습니다.")
        private Long targetUserId;

        @NotBlank(message = "정산 요청 금액은 공백일 수 없습니다.")
        private int requestedAmount;

        public PayCreateTargetInfo toPayCreateTargetInfo() {
            return PayCreateTargetInfo.builder()
                    .targetUserId(targetUserId)
                    .requestedAmount(requestedAmount)
                    .build();
        }
    }

    public PayCreateServiceRequest toServiceRequest() {
        return PayCreateServiceRequest.builder()
                .totalAmount(totalAmount)
                .bankName(bankName)
                .bankAccountNum(bankAccountNum)
                .payCreateTargetInfos(targetInfoRequests.stream()
                        .map(PayCreateTargetRequest::toPayCreateTargetInfo)
                        .toList())
                .payType(PayType.valueOf(payType))
                .build();
    }
}
