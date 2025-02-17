package space.space_spring.domain.pay.application.port.in.completePay;

public interface CompletePayUseCase {

    void completeForRequestedPay(Long targetMemberId, Long payRequestTargetId);
}
