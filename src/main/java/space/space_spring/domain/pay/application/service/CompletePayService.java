package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.completePay.CompletePayUseCase;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.application.port.out.UpdatePayPort;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.ALREADY_COMPLETE_PAY_REQUEST_TARGET;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.PAY_REQUEST_TARGET_MISMATCH;

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

        if (payRequestTarget.isComplete()) {
            throw new CustomException(ALREADY_COMPLETE_PAY_REQUEST_TARGET);
        }

        if (!payRequestTarget.isSameTargetMember(targetMemberId)) {
            throw new CustomException(PAY_REQUEST_TARGET_MISMATCH);
        }

        // 정산 완료 처리
        payRequestTarget.completeForRequestedPay();

        // 변경 사항을 DB에 업데이트
        updatePayPort.update(payRequestTarget);
    }
}
