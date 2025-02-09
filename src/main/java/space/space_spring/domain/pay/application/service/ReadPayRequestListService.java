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
@Slf4j
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
                CurrentPayRequestState currentPayRequestState = loadCurrentPayRequestStateUseCase.loadCurrentPayRequestState(payRequest.getId());
                Money receivedAmount = currentPayRequestState.getReceivedAmount();
                NaturalNumber sendCompleteTargetNum = currentPayRequestState.getSendCompleteTargetNum();

                boolean isAmountComplete = payRequest.getTotalAmount().equals(receivedAmount);      // 정산 대상들에게 받은 돈과 totalAmount 비교
                boolean isTargetComplete = payRequest.getTotalTargetNum().equals(sendCompleteTargetNum);        // 정산 대상들 중 돈 낸 사람들의 수와 totalTargetNum 비교

                if (isAmountComplete ^ isTargetComplete) {          // 두 조건 중 하나만 완료된 경우 -> db 에러가 있다고 판단된다
                    log.error("DB 에러 - 불일치된 상태 (payRequestId: {}) - totalAmount: {}, receivedAmount: {}, totalTargetNum: {}, sendCompleteTargetNum: {}",
                            payRequest.getId(), payRequest.getTotalAmount(), receivedAmount, payRequest.getTotalTargetNum(), sendCompleteTargetNum);
                    throw new CustomException(DATABASE_ERROR);
                }

                InfoOfPayRequest info = createInfoOfPayRequest(payRequest, receivedAmount, sendCompleteTargetNum);

                if (isAmountComplete && isTargetComplete) {
                    infosOfComplete.add(info);
                } else {
                    infosOfInComplete.add(info);
                }
            } catch (IllegalStateException e) {
                log.error("대상 목록 로드 실패 - 현재 payRequestId: {}", payRequest.getId(), e);
                throw new CustomException(THIS_PAY_REQUEST_HAS_NOT_TARGETS);
            }
        }

        // return 타입 구성
        return ResultOfReadPayRequestList.of(infosOfComplete, infosOfInComplete);
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
