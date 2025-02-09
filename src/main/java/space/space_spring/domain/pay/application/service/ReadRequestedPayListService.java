package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.InfoOfRequestedPay;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.ReadRequestedPayListUseCase;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.ResultOfReadRequestedPayList;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.spaceMember.LoadSpaceMemberInfoPort;
import space.space_spring.domain.spaceMember.NicknameAndProfileImage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadRequestedPayListService implements ReadRequestedPayListUseCase {

    private final LoadPayRequestTargetPort loadPayRequestTargetPort;
    private final LoadPayRequestPort loadPayRequestPort;
    private final LoadSpaceMemberInfoPort spaceMemberInfoPort;

    @Override
    public ResultOfReadRequestedPayList readRequestedPayList(Long targetMemberId) {
        // targetMember가 정산 대상자인 PayRequestTarget list 로드
        // 개수 제한 없이 일단 전부 로드
        List<PayRequestTarget> payRequestTargets = loadPayRequestTargetPort.loadByTargetMemberId(targetMemberId);

        List<InfoOfRequestedPay> infosOfComplete = new ArrayList<>();
        List<InfoOfRequestedPay> infosOfInComplete = new ArrayList<>();

        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            processPayRequestTarget(payRequestTarget, infosOfComplete, infosOfInComplete);
        }

        return ResultOfReadRequestedPayList.of(infosOfComplete, infosOfInComplete);
    }

    private void processPayRequestTarget(PayRequestTarget payRequestTarget, List<InfoOfRequestedPay> infosOfComplete, List<InfoOfRequestedPay> infosOfInComplete) {
        // 해당 PayRequestTarget에 연관된 PayRequest 로드
        PayRequest payRequest = loadPayRequestPort.loadById(payRequestTarget.getPayRequestId());

        // payCreator의 닉네임 및 프로필 이미지 정보 로드
        NicknameAndProfileImage nicknameAndProfileImage = spaceMemberInfoPort.loadNicknameAndProfileImageById(payRequest.getPayCreatorId());

        // InfoOfRequestedPay 객체 생성
        InfoOfRequestedPay info = createInfoOfRequestedPay(payRequestTarget, nicknameAndProfileImage, payRequest);

        // 완료 여부에 따라 분류하여 리스트에 추가
        if (payRequestTarget.isComplete()) {
            infosOfComplete.add(info);
        } else {
            infosOfInComplete.add(info);
        }
    }

    private InfoOfRequestedPay createInfoOfRequestedPay(PayRequestTarget payRequestTarget, NicknameAndProfileImage nicknameAndProfileImage, PayRequest payRequest) {
        return InfoOfRequestedPay.of(
                payRequestTarget.getId(),
                nicknameAndProfileImage.getNickname(),
                nicknameAndProfileImage.getProfileImageUrl(),
                payRequestTarget.getRequestedAmount(),
                payRequest.getBank()
        );
    }
}
