package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.InfoOfRequestedPay;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.ReadRequestedPayListUseCase;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.ResultOfReadRequestedPayList;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestInfoPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.domain.Bank;
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
            PayRequest payRequest = loadPayRequestPort.loadById(payRequestTarget.getPayRequestId());
            NicknameAndProfileImage nicknameAndProfileImage = spaceMemberInfoPort.loadNicknameAndProfileImageById(payRequest.getPayCreatorId());

            if (payRequestTarget.isComplete()) {
                infosOfComplete.add(InfoOfRequestedPay.of(
                        payRequestTarget.getId(),
                        nicknameAndProfileImage.getNickname(),
                        nicknameAndProfileImage.getProfileImageUrl(),
                        payRequestTarget.getRequestedAmount(),
                        payRequest.getBank()
                ));
            } else {
                infosOfInComplete.add(InfoOfRequestedPay.of(
                        payRequestTarget.getId(),
                        nicknameAndProfileImage.getNickname(),
                        nicknameAndProfileImage.getProfileImageUrl(),
                        payRequestTarget.getRequestedAmount(),
                        payRequest.getBank()
                ));
            }
        }

        return ResultOfReadRequestedPayList.of(infosOfComplete, infosOfInComplete);
    }

}
