package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.pay.application.port.in.completePay.CompletePayUseCase;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.application.port.out.UpdatePayPort;

@Service
@RequiredArgsConstructor
public class CompletePayService implements CompletePayUseCase {

    private final UpdatePayPort updatePayPort;
    private final LoadPayRequestTargetPort loadPayRequestTargetPort;

    @Override
    public void completeForRequestedPay(Long targetMemberId, Long payRequestTargetId) {
        // access token에 포함된 spaceMemberId 가 정산 타겟 유저가 맞는지 확인


        //

    }
}
