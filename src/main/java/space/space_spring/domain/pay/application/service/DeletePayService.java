package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.deletePay.DeletePayUseCase;
import space.space_spring.domain.pay.application.port.out.DeletePayRequestPort;
import space.space_spring.domain.pay.application.port.out.DeletePayRequestTargetPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayRequestTargets;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.PAY_REQUEST_CREATOR_MISMATCH;

@Service
@RequiredArgsConstructor
public class DeletePayService implements DeletePayUseCase {

    private final LoadPayRequestPort loadPayRequestPort;
    private final LoadPayRequestTargetPort loadPayRequestTargetPort;
    private final DeletePayRequestPort deletePayRequestPort;
    private final DeletePayRequestTargetPort deletePayRequestTargetPort;

    @Override
    @Transactional
    public void deletePay(Long spaceMemberId, Long payRequestId) {
        PayRequest payRequest = loadPayRequestPort.loadById(payRequestId);
        if (!payRequest.isPayCreator(spaceMemberId)) {
            throw new CustomException(PAY_REQUEST_CREATOR_MISMATCH);
        }

        PayRequestTargets payRequestTargets = PayRequestTargets.create(loadPayRequestTargetPort.loadByPayRequestId(payRequestId));
        deletePayRequestTargetPort.deleteAllPayRequestTarget(payRequestTargets.getAllTargetIds());

        deletePayRequestPort.deletePayRequest(payRequestId);
    }
}
