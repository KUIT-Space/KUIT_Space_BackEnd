package space.space_spring.domain.pay.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ResultOfReadPayRequestList;
import space.space_spring.domain.pay.application.port.in.readRequestedPayList.ResultOfReadRequestedPayList;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.domain.*;
import space.space_spring.domain.space.Space;
import space.space_spring.domain.spaceMember.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.SpaceMember;
import space.space_spring.domain.user.User;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;

class ReadRequestedPayListServiceTest {

    private final LoadSpaceMemberPort loadSpaceMemberPort = Mockito.mock(LoadSpaceMemberPort.class);
    private final LoadPayRequestTargetPort loadPayRequestTargetPort = Mockito.mock(LoadPayRequestTargetPort.class);
    private ReadRequestedPayListService readRequestedPayListService;

    private Space kuit;

    private SpaceMember seongjun;
    private SpaceMember sangjun;
    private SpaceMember seohyun;
    private SpaceMember kyeongmin;

    @BeforeEach
    void setUp() {
        readRequestedPayListService = new ReadRequestedPayListService(loadSpaceMemberPort, loadPayRequestTargetPort);
        User commonUser = User.create(1L, 1L);          // User 도메인 엔티티는 그냥 하나로 공유해서 테스트 진행
        kuit = Space.create(1L, "쿠잇", 1L);

        seongjun = SpaceMember.create(1L, kuit, commonUser, 1L, "노성준", "image_111", true);
        sangjun = SpaceMember.create(2L, kuit, commonUser, 2L, "개구리비안", "image_222", false);
        seohyun = SpaceMember.create(3L, kuit, commonUser, 3L, "정서현", "image_333", false);
        kyeongmin = SpaceMember.create(4L, kuit, commonUser, 4L, "김경민", "image_444", false);

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
    @DisplayName("특정 유저가 요청받은 모든 정산의 [payRequestTargetId, 정산 생성자 닉네임, 요청받은 금액, 송금할 은행] 정보를 송금완료한 정산, 아직 송금하지 않은 정산으로 구분해서 반환한다.")
    void readRequestedPayList1() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.createNewPayRequest(1L, kyeongmin, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest2 = PayRequest.createNewPayRequest(2L, sangjun, Money.of(20000), NaturalNumber.of(2), Bank.of("국민은행", "222-222"), PayType.INDIVIDUAL);

        PayRequestTarget payRequestTarget1 = PayRequestTarget.createNewPayRequestTarget(1L, seongjun, payRequest1, Money.of(3333));
        PayRequestTarget payRequestTarget2 = PayRequestTarget.of(2L, seongjun, payRequest2, Money.of(10000), true);

        Mockito.when(loadPayRequestTargetPort.findListByTargetMember(seongjun)).thenReturn(List.of(payRequestTarget1, payRequestTarget2));

        //when
        ResultOfReadRequestedPayList result = readRequestedPayListService.readRequestedPayList(seongjun.getId());

        //then
        assertThat(result.getCompleteRequestedPayList()).hasSize(1)
                .extracting(
                        "payRequestTargetId", "payCreatorNickname", "requestedAmount", "bank"
                )
                .containsExactlyInAnyOrder(
                        tuple(2L, sangjun.getNickname(), Money.of(10000), Bank.of("국민은행", "222-222")
                        )
                );

        assertThat(result.getInCompleteRequestedPayList()).hasSize(1)
                .extracting(
                        "payRequestTargetId", "payCreatorNickname", "requestedAmount", "bank"
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, kyeongmin.getNickname(), Money.of(3333), Bank.of("우리은행", "111-111"))
                );
    }

    @Test
    @DisplayName("특정 유저가 요청받은 정산 중 [송금완료한 정산] 이 없을 경우, 송금완료한 정산은 빈 ArrayList를 반환한다.")
    void readRequestedPayList2() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.createNewPayRequest(1L, kyeongmin, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest2 = PayRequest.createNewPayRequest(2L, sangjun, Money.of(20000), NaturalNumber.of(2), Bank.of("국민은행", "222-222"), PayType.INDIVIDUAL);

        PayRequestTarget payRequestTarget1 = PayRequestTarget.createNewPayRequestTarget(1L, seongjun, payRequest1, Money.of(3333));
        PayRequestTarget payRequestTarget2 = PayRequestTarget.createNewPayRequestTarget(2L, seongjun, payRequest2, Money.of(10000));

        Mockito.when(loadPayRequestTargetPort.findListByTargetMember(seongjun)).thenReturn(List.of(payRequestTarget1, payRequestTarget2));

        //when
        ResultOfReadRequestedPayList result = readRequestedPayListService.readRequestedPayList(seongjun.getId());

        //then
        assertThat(result.getCompleteRequestedPayList()).isNotNull().isEmpty();

        assertThat(result.getInCompleteRequestedPayList()).hasSize(2)
                .extracting(
                        "payRequestTargetId", "payCreatorNickname", "requestedAmount", "bank"
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, kyeongmin.getNickname(), Money.of(3333), Bank.of("우리은행", "111-111")),
                        tuple(2L, sangjun.getNickname(), Money.of(10000), Bank.of("국민은행", "222-222"))
                );
    }

    @Test
    @DisplayName("특정 유저가 요청받은 정산 중 [송금완료하지 않은 정산] 이 없을 경우, 송금완료하지 않은 정산은 빈 ArrayList를 반환한다.")
    void readRequestedPayList3() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.createNewPayRequest(1L, kyeongmin, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest2 = PayRequest.createNewPayRequest(2L, sangjun, Money.of(20000), NaturalNumber.of(2), Bank.of("국민은행", "222-222"), PayType.INDIVIDUAL);

        PayRequestTarget payRequestTarget1 = PayRequestTarget.of(1L, seongjun, payRequest1, Money.of(3333), true);
        PayRequestTarget payRequestTarget2 = PayRequestTarget.of(2L, seongjun, payRequest2, Money.of(10000), true);

        Mockito.when(loadPayRequestTargetPort.findListByTargetMember(seongjun)).thenReturn(List.of(payRequestTarget1, payRequestTarget2));

        //when
        ResultOfReadRequestedPayList result = readRequestedPayListService.readRequestedPayList(seongjun.getId());

        //then
        assertThat(result.getCompleteRequestedPayList()).hasSize(2)
                .extracting(
                        "payRequestTargetId", "payCreatorNickname", "requestedAmount", "bank"
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, kyeongmin.getNickname(), Money.of(3333), Bank.of("우리은행", "111-111")),
                        tuple(2L, sangjun.getNickname(), Money.of(10000), Bank.of("국민은행", "222-222"))
                );

        assertThat(result.getInCompleteRequestedPayList()).isNotNull().isEmpty();
    }
}