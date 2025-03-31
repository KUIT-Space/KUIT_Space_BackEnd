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
            boolean isComplete = payRequest.isEqualToTotalTargetNum(currentState.getSendCompleteTargetNum());         // 완료된 정산 == 모든 정산 타겟들이 송금 완료하였다
            InfoOfPayRequest info = createInfo(payRequest, currentState, isComplete);
            addInfoToProperList(isComplete, info, infosOfComplete, infosOfInComplete);
        } catch (IllegalStateException e) {
            log.error("정산 대상 목록 로드 실패 - 현재 payRequestId: {}", payRequest.getId(), e);
            throw new CustomException(THIS_PAY_REQUEST_HAS_NOT_TARGETS);
        }
    }

    private InfoOfPayRequest createInfo(PayRequest payRequest, CurrentPayRequestState currentState, boolean isComplete) {
        // 나머지 금액이 있을 수 있으므로 완료된 정산에서의 receivedAmount = payRequest.totalAmount 로 설정
        Money calculatedSumOfAmount = isComplete ? payRequest.getTotalAmount() : currentState.getReceivedAmount();

        return InfoOfPayRequest.of(
                payRequest.getId(),
                payRequest.getTotalAmount(),
                calculatedSumOfAmount,
                payRequest.getTotalTargetNum(),
                currentState.getSendCompleteTargetNum()
        );
    }

    private void addInfoToProperList(boolean isComplete, InfoOfPayRequest info, List<InfoOfPayRequest> infosOfComplete, List<InfoOfPayRequest> infosOfInComplete) {
        if (isComplete) {
            infosOfComplete.add(info);
        } else {
            infosOfInComplete.add(info);
        }
    }

}
