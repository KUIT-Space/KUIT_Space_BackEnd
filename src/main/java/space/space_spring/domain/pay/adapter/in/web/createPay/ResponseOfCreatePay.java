package space.space_spring.domain.pay.adapter.in.web.createPay;

import lombok.Getter;

@Getter
public class ResponseOfCreatePay {

    private Long payRequestId;

    private ResponseOfCreatePay(Long payRequestId) {
        this.payRequestId = payRequestId;
    }

    public static ResponseOfCreatePay of(Long payRequestId) {
        return new ResponseOfCreatePay(payRequestId);
    }
}
