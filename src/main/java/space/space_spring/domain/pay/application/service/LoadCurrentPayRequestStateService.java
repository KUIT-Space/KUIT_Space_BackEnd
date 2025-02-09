package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.CurrentPayRequestState;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.LoadCurrentPayRequestStateUseCase;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.InfoOfPayRequest;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequestTargets;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.NaturalNumber;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.DATABASE_ERROR;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.THIS_PAY_REQUEST_HAS_NOT_TARGETS;

@Service
@RequiredArgsConstructor
public class LoadCurrentPayRequestStateService implements LoadCurrentPayRequestStateUseCase {

    private final LoadPayRequestTargetPort loadPayRequestTargetPort;

    @Override
    public CurrentPayRequestState loadCurrentPayRequestState(Long payRequestId) {
        try {
            PayRequestTargets payRequestTargets = PayRequestTargets.create(loadPayRequestTargetPort.loadByPayRequestId(payRequestId));

            Money receivedAmount = payRequestTargets.calculateMoneyOfSendComplete();
            NaturalNumber sendCompleteTargetNum = payRequestTargets.calculateNumberOfSendCompleteTarget();

            return CurrentPayRequestState.of(receivedAmount, sendCompleteTargetNum);
        } catch (IllegalStateException e) {
            System.err.println("현재 payRequestId: " + payRequestId);
            throw new CustomException(THIS_PAY_REQUEST_HAS_NOT_TARGETS);
        }
    }
}
