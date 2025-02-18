package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.completePay.CompletePayUseCase;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.application.port.out.UpdatePayPort;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_PAY_REQUEST_TARGET_ID;

@Service
@RequiredArgsConstructor
public class CompletePayService implements CompletePayUseCase {

    private final UpdatePayPort updatePayPort;
    private final LoadPayRequestTargetPort loadPayRequestTargetPort;

    @Override
    @Transactional
    public void completeForRequestedPay(Long targetMemberId, Long payRequestTargetId) {
        // access token에 포함된 spaceMemberId 가 정산 타겟 유저가 맞는지 확인
        PayRequestTarget payRequestTarget = loadPayRequestTargetPort.loadById(payRequestTargetId);

        if (!payRequestTarget.isSameTargetMember(targetMemberId)) {
            throw new CustomException(INVALID_PAY_REQUEST_TARGET_ID);
        }

        // 정산 완료 처리
        payRequestTarget.completeForRequestedPay();

        // 디스코드로 변경된 정보 보내기 (상준님과 추후 협의)

        // 변경 사항을 DB에 업데이트
        updatePayPort.update(payRequestTarget);
    }
}
