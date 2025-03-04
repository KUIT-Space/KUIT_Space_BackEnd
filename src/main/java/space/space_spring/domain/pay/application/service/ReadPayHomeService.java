package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.application.port.in.readPayHome.InfoOfPayRequestInHome;
import space.space_spring.domain.pay.application.port.in.readPayHome.InfoOfRequestedPayInHome;
import space.space_spring.domain.pay.application.port.in.readPayHome.ReadPayHomeUseCase;
import space.space_spring.domain.pay.application.port.in.readPayHome.ResultOfPayHomeView;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ReadPayRequestListUseCase;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ResultOfReadPayRequestList;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.ReadRequestedPayListUseCase;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.ResultOfReadRequestedPayList;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadPayHomeService implements ReadPayHomeUseCase {

    private final ReadPayRequestListUseCase readPayRequestListUseCase;
    private final ReadRequestedPayListUseCase readRequestedPayListUseCase;

    @Override
    public ResultOfPayHomeView readPayHome(Long spaceMemberId) {
        // 1. spaceMember가 요청한 정산 중 현재 진행중인 정산 로드
        ResultOfReadPayRequestList resultOfReadPayRequestList = readPayRequestListUseCase.readPayRequestList(spaceMemberId);

        // 2. spaceMember가 요청받은 정산 중 현재 진행중인 정산 로드
        ResultOfReadRequestedPayList resultOfReadRequestedPayList = readRequestedPayListUseCase.readRequestedPayList(spaceMemberId);

        // 3. complete와 inComplete 목록을 병합하여 Home용 DTO로 변환
        List<InfoOfPayRequestInHome> infoOfPayRequestInHomes = convertToInfoOfPayRequestInHome(resultOfReadPayRequestList);
        List<InfoOfRequestedPayInHome> infoOfRequestedPayInHomes = convertToInfoOfRequestedPayInHome(resultOfReadRequestedPayList);

        // 4. 최종 결과 객체 생성 및 반환
        return ResultOfPayHomeView.of(infoOfPayRequestInHomes, infoOfRequestedPayInHomes);
    }

    private List<InfoOfPayRequestInHome> convertToInfoOfPayRequestInHome(ResultOfReadPayRequestList result) {
        return result.getInCompletePayRequestList().stream()        // 현재 진행 중인 정산의 정보만 get
                .map(info -> InfoOfPayRequestInHome.of(
                        info.getPayRequestId(),
                        info.getTotalAmount(),
                        info.getReceivedAmount(),
                        info.getTotalTargetNum(),
                        info.getSendCompleteTargetNum()
                ))
                .toList();
    }

    private List<InfoOfRequestedPayInHome> convertToInfoOfRequestedPayInHome(ResultOfReadRequestedPayList result) {
        return result.getInCompleteRequestedPayList().stream()      // 현재 진행 중인 정산의 정보만 get
                .map(info -> InfoOfRequestedPayInHome.of(
                        info.getPayRequestTargetId(),
                        info.getPayCreatorNickname(),
                        info.getPayCreatorProfileImageUrl(),
                        info.getRequestedAmount(),
                        info.getBank()
                ))
                .toList();
    }
}
