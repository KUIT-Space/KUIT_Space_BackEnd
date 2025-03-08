package space.space_spring.domain.pay.adapter.in.web.createPay;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.global.common.validation.SelfValidating;
import space.space_spring.global.validator.EnumValidator;

import java.util.List;

@NoArgsConstructor
@Getter
public class RequestOfCreatePay extends SelfValidating<RequestOfCreatePay> {

    /**
     * 웹 어댑터의 입력 모델 -> 입력 유효성 검사
     */

    @NotBlank(message = "총 정산 요청 금액은 공백일 수 없습니다.")
    @Positive(message = "총 정산 요청 금액은 양수이어야 합니다.")
    private int totalAmount;

    @NotBlank(message = "은행 이름은 공백일 수 없습니다.")
    private String bankName;

    @NotBlank(message = "은행 계좌 번호는 공백일 수 없습니다.")
    private String bankAccountNum;

    @Valid
    private List<TargetOfPayRequest> targets;

    @EnumValidator(enumClass = PayType.class, message = "payType은 INDIVIDUAL 또는 EQUAL_SPLIT 이어야 합니다.")
    private String valueOfPayType;

    public RequestOfCreatePay(int totalAmount, String bankName, String bankAccountNum, List<TargetOfPayRequest> targets, String valueOfPayType) {
        this.totalAmount = totalAmount;
        this.bankName = bankName;
        this.bankAccountNum = bankAccountNum;
        this.targets = targets;
        this.valueOfPayType = valueOfPayType;
        this.validateSelf();
    }
}
