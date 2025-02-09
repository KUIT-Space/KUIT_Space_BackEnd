package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.CurrentPayRequestState;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.LoadCurrentPayRequestStateUseCase;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.InfoOfPayRequest;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ReadPayRequestListUseCase;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ResultOfReadPayRequestList;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.domain.*;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.NaturalNumber;

import java.util.ArrayList;
import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.DATABASE_ERROR;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.THIS_PAY_REQUEST_HAS_NOT_TARGETS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadPayRequestListService implements ReadPayRequestListUseCase {

    private final LoadPayRequestPort loadPayRequestPort;
    private final LoadCurrentPayRequestStateUseCase loadCurrentPayRequestStateUseCase;

    @Override
    public ResultOfReadPayRequestList readPayRequestList(Long payCreatorId) {
        // payCreator가 요청한 PayRequest list 로드
        // 개수 제한 없이 일단 전부 로드
        List<PayRequest> payRequests = loadPayRequestPort.loadByPayCreatorId(payCreatorId);

        List<InfoOfPayRequest> infosOfComplete = new ArrayList<>();
        List<InfoOfPayRequest> infosOfInComplete = new ArrayList<>();

        for (PayRequest payRequest : payRequests) {
            try {
                Money totalAmount = payRequest.getTotalAmount();
                NaturalNumber totalTargetNum = payRequest.getTotalTargetNum();

                CurrentPayRequestState currentPayRequestState = loadCurrentPayRequestStateUseCase.loadCurrentPayRequestState(payRequest.getId());
                Money receivedAmount = currentPayRequestState.getReceivedAmount();
                NaturalNumber sendCompleteTargetNum = currentPayRequestState.getSendCompleteTargetNum();

                if (totalAmount.equals(receivedAmount) && totalTargetNum.equals(sendCompleteTargetNum)) {
                    infosOfComplete.add(InfoOfPayRequest.of(
                            payRequest.getId(),
                            totalAmount,
                            receivedAmount,
                            totalTargetNum,
                            sendCompleteTargetNum
                    ));
                } else if ((totalAmount.equals(receivedAmount) && !totalTargetNum.equals(sendCompleteTargetNum)) || (!totalAmount.equals(receivedAmount) && totalTargetNum.equals(sendCompleteTargetNum))) {
                    System.err.println("db 에러 payRequestId : " + payRequest.getId());
                    throw new CustomException(DATABASE_ERROR);
                } else {
                    infosOfInComplete.add(InfoOfPayRequest.of(
                            payRequest.getId(),
                            totalAmount,
                            receivedAmount,
                            totalTargetNum,
                            sendCompleteTargetNum
                    ));
                }
            } catch (IllegalStateException e) {
                System.err.println("현재 payRequestId: " + payRequest.getId());
                throw new CustomException(THIS_PAY_REQUEST_HAS_NOT_TARGETS);
            }
        }

        // return 타입 구성
        return ResultOfReadPayRequestList.of(infosOfComplete, infosOfInComplete);
    }
}
