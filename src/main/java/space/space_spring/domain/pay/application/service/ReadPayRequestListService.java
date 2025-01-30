package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.InfoOfPayRequest;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ReadPayRequestListUseCase;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ResultOfReadPayRequestList;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayRequests;
import space.space_spring.domain.spaceMember.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.SpaceMember;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadPayRequestListService implements ReadPayRequestListUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadPayRequestPort loadPayRequestPort;

    @Override
    public ResultOfReadPayRequestList readPayRequestList(Long payCreatorId) {
        // 정산 생성자 로드
        SpaceMember payCreator = loadSpaceMemberPort.loadSpaceMember(payCreatorId);

        // payCreator가 요청한 PayRequest list 로드
        // 개수 제한 없이 일단 전부 로드
        PayRequests payRequests = PayRequests.create(loadPayRequestPort.findListByCreator(payCreator));

        // return 타입 구성
        List<PayRequest> completePayRequests = payRequests.getCompletePayRequestList();
        List<PayRequest> inCompletePayRequests = payRequests.getInCompletePayRequestList();

        List<InfoOfPayRequest> infosOfComplete = new ArrayList<>();
        for (PayRequest payRequest : completePayRequests) {
            infosOfComplete.add(InfoOfPayRequest.of(
                    payRequest.getPayCreator().getId(),
                    payRequest.getTotalAmount(),
                    payRequest.getReceivedAmount(),
                    payRequest.getTotalTargetNum(),
                    payRequest.getSendCompleteTargetNum()
            ));
        }

        List<InfoOfPayRequest> infosOfInComplete = new ArrayList<>();
        for (PayRequest payRequest : inCompletePayRequests) {
            infosOfInComplete.add(InfoOfPayRequest.of(
                    payRequest.getPayCreator().getId(),
                    payRequest.getTotalAmount(),
                    payRequest.getReceivedAmount(),
                    payRequest.getTotalTargetNum(),
                    payRequest.getSendCompleteTargetNum()
            ));
        }

        return ResultOfReadPayRequestList.of(infosOfComplete, infosOfInComplete);
    }
}
