package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.CurrentPayRequestState;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.LoadCurrentPayRequestStateUseCase;
import space.space_spring.domain.pay.application.port.in.readPayDetail.InfoOfTargetDetail;
import space.space_spring.domain.pay.application.port.in.readPayDetail.ReadPayDetailUseCase;
import space.space_spring.domain.pay.application.port.in.readPayDetail.ResultOfReadPayDetail;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberInfoPort;
import space.space_spring.domain.spaceMember.application.port.out.NicknameAndProfileImage;
import space.space_spring.global.exception.CustomException;

import java.time.LocalDateTime;
import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_PAY_REQUEST_ID;

@Service
@RequiredArgsConstructor
public class ReadPayDetailService implements ReadPayDetailUseCase {

    private final LoadPayRequestPort loadPayRequestPort;
    private final LoadPayRequestTargetPort loadPayRequestTargetPort;
    private final LoadCurrentPayRequestStateUseCase loadCurrentPayRequestStateUseCase;
    private final LoadSpaceMemberInfoPort loadSpaceMemberInfoPort;

    @Override
    public ResultOfReadPayDetail readPayDetail(Long spaceMemberId, Long payRequestId) {
        // 1. 정산 요청 로드 및 정산 생성자 검증
        PayRequest payRequest = loadAndValidatePayRequest(spaceMemberId, payRequestId);

        // 2. 해당 정산 요청에 대한 정산 대상들 로드
        List<PayRequestTarget> payRequestTargets = loadPayRequestTargetPort.loadByPayRequestId(payRequest.getId());

        // 3. 현재 정산 요청의 State 로드
        CurrentPayRequestState currentPayRequestState = loadCurrentPayRequestStateUseCase.loadCurrentPayRequestState(payRequest.getId());

        // 4. 정산 대상들에 대한 상세 정보 생성
        List<InfoOfTargetDetail> targetDetails = buildTargetDetails(payRequestTargets);

        // 5. return
        return buildResult(payRequest, currentPayRequestState, targetDetails);
    }

    private PayRequest loadAndValidatePayRequest(Long spaceMemberId, Long payRequestId) {
        PayRequest payRequest = loadPayRequestPort.loadById(payRequestId);
        if (!payRequest.isPayCreator(spaceMemberId)) {
            throw new CustomException(INVALID_PAY_REQUEST_ID);
        }
        return payRequest;
    }

    private List<InfoOfTargetDetail> buildTargetDetails(List<PayRequestTarget> payRequestTargets) {
        return payRequestTargets.stream()
                .map(target -> {
                    NicknameAndProfileImage nicknameAndProfileImage = loadSpaceMemberInfoPort.loadNicknameAndProfileImageById(target.getTargetMemberId());
                    return InfoOfTargetDetail.of(
                            target.getTargetMemberId(),
                            nicknameAndProfileImage.getNickname(),
                            nicknameAndProfileImage.getProfileImageUrl(),
                            target.getRequestedAmount(),
                            target.isComplete()
                    );
                })
                .toList();
    }

    private ResultOfReadPayDetail buildResult(PayRequest payRequest, CurrentPayRequestState currentState, List<InfoOfTargetDetail> targetDetails) {
        return ResultOfReadPayDetail.builder()
                .payRequestId(payRequest.getId())
                .totalAmount(payRequest.getTotalAmount())
                .receivedAmount(currentState.getReceivedAmount())
                .totalTargetNum(payRequest.getTotalTargetNum())
                .sendCompleteTargetNum(currentState.getSendCompleteTargetNum())
                .bank(payRequest.getBank())
                .createDate(LocalDateTime.now())  // TODO: Base 도메인 엔티티 생성 후 수정
                .infoOfTargetDetails(targetDetails)
                .build();
    }
}
