package space.space_spring.domain.pay.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.pay.model.PayType;
import space.space_spring.global.validator.EnumValidator;

import java.util.List;

@Getter
@NoArgsConstructor
public class PayCreateRequest {

    /**
     * PayRequest 엔티티 생성 시 필요한 정보
     */

    @NotNull(message = "총 정산 요청 금액은 공백일 수 없습니다.")
    @Positive(message = "총 정산 요청 금액은 양수이어야 합니다.")
    private int totalAmount;

    @NotBlank(message = "은행 이름은 공백일 수 없습니다.")
    private String bankName;

    @NotBlank(message = "은행 계좌 번호는 공백일 수 없습니다.")
    private String bankAccountNum;

    @Valid
    private List<TargetInfoRequest> targetInfoRequests;

    @EnumValidator(enumClass = PayType.class, message = "payType은 INDIVIDUAL 또는 EQUAL_SPLIT 이어야 합니다.")
    private String payType;


    @Builder
    private PayCreateRequest(int totalAmount, String bankName, String bankAccountNum, List<TargetInfoRequest> targetInfoRequests, String payType) {
        this.totalAmount = totalAmount;
        this.bankName = bankName;
        this.bankAccountNum = bankAccountNum;
        this.targetInfoRequests = targetInfoRequests;
        this.payType = payType;
    }

    public PayCreateServiceRequest toServiceRequest() {
        return PayCreateServiceRequest.builder()
                .totalAmount(totalAmount)
                .bankName(bankName)
                .bankAccountNum(bankAccountNum)
                .payCreateTargetInfos(targetInfoRequests.stream()
                        .map(TargetInfoRequest::toPayCreateTargetInfo)
                        .toList())
                .payType(PayType.valueOf(payType))
                .build();
    }
}
