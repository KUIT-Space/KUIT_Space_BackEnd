package space.space_spring.domain.pay.application.port.in.deletePay;

public interface DeletePayUseCase {

    void deletePay(Long spaceMemberId, Long payRequestId);
}
