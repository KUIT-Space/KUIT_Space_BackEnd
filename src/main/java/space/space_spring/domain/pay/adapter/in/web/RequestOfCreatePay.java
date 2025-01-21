package space.space_spring.domain.pay.adapter.in.web;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class RequestOfCreatePay {

    /**
     * 웹 어댑터의 입력 모델 -> 입력 유효성 검사
     * not null 등등
     */

    private int totalAmount;

    private String bankName;

    private String bankAccountNum;

    private List<TargetOfPayRequest> targets;

    private String payType;
}
