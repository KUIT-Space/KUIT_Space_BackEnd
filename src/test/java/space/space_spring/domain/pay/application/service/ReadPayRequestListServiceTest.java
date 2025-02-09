package space.space_spring.domain.pay.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ResultOfReadPayRequestList;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ReadPayRequestListServiceTest {

    private final LoadPayRequestPort loadPayRequestPort = Mockito.mock(LoadPayRequestPort.class);
    private final LoadPayRequestTargetPort loadPayRequestTargetPort = Mockito.mock(LoadPayRequestTargetPort.class);
    private ReadPayRequestListService readPayRequestListService;

    @BeforeEach
    void setUp() {
        readPayRequestListService = new ReadPayRequestListService(loadPayRequestPort, loadPayRequestTargetPort);
    }

    @Test
    @DisplayName("특정 유저가 생성한 모든 정산의 [payRequestId, 전체 정산 요청 금액, 정산 받은 금액, 전체 정산 대상자 수, 그 중 정산 완료한 사람 수] 정보를 완료된 정산, 진행중인 정산으로 구분해서 반환한다.")
    void readPayRequestList1() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.create(1L, seongjun, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest2 = PayRequest.create(2L, seongjun, Money.of(20000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.INDIVIDUAL);
        PayRequest payRequest3 = PayRequest.of(3L, seongjun, Money.of(20000), Money.of(20000), NaturalNumber.of(2), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), true, PayType.INDIVIDUAL);
        Mockito.when(loadPayRequestPort.loadByPayCreatorId(seongjun)).thenReturn(List.of(payRequest1, payRequest2, payRequest3));

        //when
        ResultOfReadPayRequestList result = readPayRequestListService.readPayRequestList(seongjun.getId());

        //then
        assertThat(result.getCompletePayRequestList()).hasSize(1)
                .extracting(
                        "payRequestId", "totalAmount", "receivedAmount", "totalTargetNum", "sendCompleteTargetNum"
                )
                .containsExactlyInAnyOrder(
                        tuple(3L, Money.of(20000), Money.of(20000), NaturalNumber.of(2), NaturalNumber.of(2))
                );

        assertThat(result.getInCompletePayRequestList()).hasSize(2)
                .extracting(
                        "payRequestId", "totalAmount", "receivedAmount", "totalTargetNum", "sendCompleteTargetNum"
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, Money.of(10000), Money.of(0), NaturalNumber.of(3), NaturalNumber.of(0)),
                        tuple(2L, Money.of(20000), Money.of(0), NaturalNumber.of(2), NaturalNumber.of(0))
                );
    }

    @Test
    @DisplayName("특정 유저가 생성한 정산 중 [현재 진행중인 정산] 이 없을 경우, 현재 진행중인 정산은 빈 ArrayList를 반환한다.")
    void readPayRequestList2() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.of(1L, seongjun, Money.of(10000), Money.of(10000), NaturalNumber.of(3), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), true, PayType.EQUAL_SPLIT);
        PayRequest payRequest2 = PayRequest.of(2L, seongjun, Money.of(20000), Money.of(20000), NaturalNumber.of(2), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), true, PayType.INDIVIDUAL);
        Mockito.when(loadPayRequestPort.loadByPayCreatorId(seongjun)).thenReturn(List.of(payRequest1, payRequest2));

        //when
        ResultOfReadPayRequestList result = readPayRequestListService.readPayRequestList(seongjun.getId());

        //then
        assertThat(result.getCompletePayRequestList()).hasSize(2)
                .extracting(
                        "payRequestId", "totalAmount", "receivedAmount", "totalTargetNum", "sendCompleteTargetNum"
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, Money.of(10000), Money.of(10000), NaturalNumber.of(3), NaturalNumber.of(3)),
                        tuple(2L, Money.of(20000), Money.of(20000), NaturalNumber.of(2), NaturalNumber.of(2))
                );

        assertThat(result.getInCompletePayRequestList()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("특정 유저가 생성한 정산 중 [완료된 정산] 이 없을 경우, 완료된 정산은 빈 ArrayList를 반환한다.")
    void readPayRequestList3() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.create(1L, seongjun, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest2 = PayRequest.create(2L, seongjun, Money.of(20000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.INDIVIDUAL);
        Mockito.when(loadPayRequestPort.loadByPayCreatorId(seongjun)).thenReturn(List.of(payRequest1, payRequest2));

        //when
        ResultOfReadPayRequestList result = readPayRequestListService.readPayRequestList(seongjun.getId());

        //then
        assertThat(result.getCompletePayRequestList()).isNotNull().isEmpty();

        assertThat(result.getInCompletePayRequestList()).hasSize(2)
                .extracting(
                        "payRequestId", "totalAmount", "receivedAmount", "totalTargetNum", "sendCompleteTargetNum"
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, Money.of(10000), Money.of(0), NaturalNumber.of(3), NaturalNumber.of(0)),
                        tuple(2L, Money.of(20000), Money.of(0), NaturalNumber.of(2), NaturalNumber.of(0))
                );
    }

}