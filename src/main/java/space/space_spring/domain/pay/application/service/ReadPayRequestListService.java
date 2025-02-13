package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.CurrentPayRequestState;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.LoadCurrentPayRequestStateUseCase;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.InfoOfPayRequest;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ReadPayRequestListUseCase;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ResultOfReadPayRequestList;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;

import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.NaturalNumber;


import java.util.ArrayList;
import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.DATABASE_ERROR;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.THIS_PAY_REQUEST_HAS_NOT_TARGETS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReadPayRequestListService implements ReadPayRequestListUseCase {

    private final LoadPayRequestPort loadPayRequestPort;
    private final LoadCurrentPayRequestStateUseCase loadCurrentPayRequestStateUseCase;

    @Override
    public ResultOfReadPayRequestList readPayRequestList(Long payCreatorId) {
        // 1. payCreator가 요청한 PayRequest 목록을 로드
        List<PayRequest> payRequests = loadPayRequestPort.loadByPayCreatorId(payCreatorId);

        // 2. 각 PayRequest를 처리하여 결과 분류
        List<InfoOfPayRequest> infosOfComplete = new ArrayList<>();
        List<InfoOfPayRequest> infosOfInComplete = new ArrayList<>();
        for (PayRequest payRequest : payRequests) {
            processPayRequest(payRequest, infosOfComplete, infosOfInComplete);
        }

        return ResultOfReadPayRequestList.of(infosOfComplete, infosOfInComplete);
    }

    private void processPayRequest(PayRequest payRequest, List<InfoOfPayRequest> infosOfComplete, List<InfoOfPayRequest> infosOfInComplete) {
        try {
            CurrentPayRequestState currentState = loadCurrentPayRequestStateUseCase.loadCurrentPayRequestState(payRequest.getId());
            Money receivedAmount = currentState.getReceivedAmount();
            NaturalNumber sendCompleteTargetNum = currentState.getSendCompleteTargetNum();

            // 완료 여부 체크 (두 조건이 모두 충족되어야 완료로 판단)
            boolean isComplete = checkCompletionStatus(payRequest, receivedAmount, sendCompleteTargetNum);

            InfoOfPayRequest info = createInfoOfPayRequest(payRequest, receivedAmount, sendCompleteTargetNum);

            if (isComplete) {
                infosOfComplete.add(info);
            } else {
                infosOfInComplete.add(info);
            }
        } catch (IllegalStateException e) {
            log.error("정산 대상 목록 로드 실패 - 현재 payRequestId: {}", payRequest.getId(), e);
            throw new CustomException(THIS_PAY_REQUEST_HAS_NOT_TARGETS);
        }
    }

    private boolean checkCompletionStatus(PayRequest payRequest, Money receivedAmount, NaturalNumber sendCompleteTargetNum) {
        boolean isAmountComplete = payRequest.isEqualToTotalAmount(receivedAmount);
        boolean isTargetComplete = payRequest.isEqualToTotalTargetNum(sendCompleteTargetNum);

        if (isAmountComplete ^ isTargetComplete) {      // XOR: 한쪽만 true인 경우
            log.error("DB 에러 - 불일치된 상태 (payRequestId: {}) - totalAmount: {}, receivedAmount: {}, totalTargetNum: {}, sendCompleteTargetNum: {}",
                    payRequest.getId(), payRequest.getTotalAmount(), receivedAmount, payRequest.getTotalTargetNum(), sendCompleteTargetNum);
            throw new CustomException(DATABASE_ERROR);
        }
        return isAmountComplete && isTargetComplete;
    }

    private InfoOfPayRequest createInfoOfPayRequest(PayRequest payRequest, Money receivedAmount, NaturalNumber sendCompleteTargetNum) {
        return InfoOfPayRequest.of(
                payRequest.getId(),
                payRequest.getTotalAmount(),
                receivedAmount,
                payRequest.getTotalTargetNum(),
                sendCompleteTargetNum
        );
    }
}
