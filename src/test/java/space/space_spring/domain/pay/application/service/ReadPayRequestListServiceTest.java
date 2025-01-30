package space.space_spring.domain.pay.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ResultOfReadPayRequestList;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.domain.space.Space;
import space.space_spring.domain.spaceMember.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.SpaceMember;
import space.space_spring.domain.user.User;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class ReadPayRequestListServiceTest {

    private final LoadSpaceMemberPort loadSpaceMemberPort = Mockito.mock(LoadSpaceMemberPort.class);
    private final LoadPayRequestPort loadPayRequestPort = Mockito.mock(LoadPayRequestPort.class);
    private ReadPayRequestListService readPayRequestListService;

    private Space kuit;

    private SpaceMember seongjun;
    private SpaceMember sangjun;
    private SpaceMember seohyun;
    private SpaceMember kyeongmin;

    @BeforeEach
    void setUp() {
        readPayRequestListService = new ReadPayRequestListService(loadSpaceMemberPort, loadPayRequestPort);
        User commonUser = User.create(1L, 1L);          // User 도메인 엔티티는 그냥 하나로 공유해서 테스트 진행
        kuit = Space.create(1L, "쿠잇", 1L);

        seongjun = SpaceMember.create(1L, kuit, commonUser, 1L);
        sangjun = SpaceMember.create(2L, kuit, commonUser, 2L);
        seohyun = SpaceMember.create(3L, kuit, commonUser, 3L);
        kyeongmin = SpaceMember.create(4L, kuit, commonUser, 4L);

        // Mockito Stubbing : 특정 ID가 들어오면 그에 맞는 SpaceMember 반환
        Mockito.when(loadSpaceMemberPort.loadSpaceMember(seongjun.getId()))
                .thenReturn(seongjun);
        Mockito.when(loadSpaceMemberPort.loadSpaceMember(sangjun.getId()))
                .thenReturn(sangjun);
        Mockito.when(loadSpaceMemberPort.loadSpaceMember(seohyun.getId()))
                .thenReturn(seohyun);
        Mockito.when(loadSpaceMemberPort.loadSpaceMember(kyeongmin.getId()))
                .thenReturn(kyeongmin);
    }

    @Test
    @DisplayName("특정 유저가 생성한 모든 정산의 [payRequestId, 전체 정산 요청 금액, 정산 받은 금액, 전체 정산 대상자 수, 그 중 정산 완료한 사람 수] 정보를 완료된 정산, 진행중인 정산으로 구분해서 리턴한다.")
    void readPayRequestList1() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.create(seongjun, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest2 = PayRequest.create(seongjun, Money.of(20000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.INDIVIDUAL);
        Mockito.when(loadPayRequestPort.findListByCreator(seongjun)).thenReturn(List.of(payRequest1, payRequest2));

        //when
        ResultOfReadPayRequestList result = readPayRequestListService.readPayRequestList(1L);

        //then
        assertThat(result.getCompletePayRequestList()).isEmpty();
        assertThat(result.getInCompletePayRequestList()).hasSize(2)
                .extracting(
                        "totalAmount", "receivedAmount", "totalTargetNum", "sendCompleteTargetNum"
                )
                .containsExactlyInAnyOrder(
                        tuple(Money.of(10000), Money.of(0), NaturalNumber.of(3), NaturalNumber.of(0)),
                        tuple(Money.of(20000), Money.of(0), NaturalNumber.of(2), NaturalNumber.of(0))
                );
    }

}