package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.CurrentPayRequestState;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.LoadCurrentPayRequestStateUseCase;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.pay.domain.PayRequestTargets;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.THIS_PAY_REQUEST_HAS_NOT_TARGETS;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoadCurrentPayRequestStateService implements LoadCurrentPayRequestStateUseCase {

    private final LoadPayRequestTargetPort loadPayRequestTargetPort;

    @Override
    public CurrentPayRequestState loadCurrentPayRequestState(Long payRequestId) {
        List<PayRequestTarget> targets = loadPayRequestTargetPort.loadByPayRequestId(payRequestId);
        if (targets.isEmpty()) {
            log.error("payRequestId: {} 에 해당하는 PayRequestTarget 이 존재하지 않습니다.", payRequestId);
            throw new CustomException(THIS_PAY_REQUEST_HAS_NOT_TARGETS);
        }

        PayRequestTargets payRequestTargets = PayRequestTargets.create(targets);

        Money receivedAmount = payRequestTargets.calculateMoneyOfSendComplete();
        NaturalNumber sendCompleteTargetNum = payRequestTargets.calculateNumberOfSendCompleteTarget();

        return CurrentPayRequestState.of(receivedAmount, sendCompleteTargetNum);
    }
}
