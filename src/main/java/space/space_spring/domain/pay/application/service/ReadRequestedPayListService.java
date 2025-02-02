package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.InfoOfRequestedPay;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.ReadRequestedPayListUseCase;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.ResultOfReadRequestedPayList;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.pay.domain.PayRequestTargets;
import space.space_spring.domain.spaceMember.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.SpaceMember;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadRequestedPayListService implements ReadRequestedPayListUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadPayRequestTargetPort loadPayRequestTargetPort;

    @Override
    public ResultOfReadRequestedPayList readRequestedPayList(Long targetMemberId) {
        // 정산 대상자 로드
        SpaceMember targetMember = loadSpaceMemberPort.loadSpaceMember(targetMemberId);

        // targetMember가 정산 대상자인 PayRequestTarget list 로드
        // 개수 제한 없이 일단 전부 로드
        PayRequestTargets payRequestTargets = PayRequestTargets.create(loadPayRequestTargetPort.findListByTargetMember(targetMember));

        // return 타입 구성
        List<InfoOfRequestedPay> infosOfComplete = mapToInfoOfRequestedPays(payRequestTargets.getCompletePayRequestTargetList());
        List<InfoOfRequestedPay> infosOfInComplete = mapToInfoOfRequestedPays(payRequestTargets.getInCompletePayRequestTargetList());

        return ResultOfReadRequestedPayList.of(infosOfComplete, infosOfInComplete);
    }

    private List<InfoOfRequestedPay> mapToInfoOfRequestedPays(List<PayRequestTarget> payRequestTargets) {
        List<InfoOfRequestedPay> infosOfPayRequestTarget = new ArrayList<>();
        for (PayRequestTarget payRequestTarget : payRequestTargets) {
            infosOfPayRequestTarget.add(InfoOfRequestedPay.of(
                    payRequestTarget.getId(),
                    payRequestTarget.getPayRequest().getPayCreator().getNickname(),
                    payRequestTarget.getRequestedAmount(),
                    payRequestTarget.getPayRequest().getBank()
            ));
        }
        return infosOfPayRequestTarget;
    }
}
