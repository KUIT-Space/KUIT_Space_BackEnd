package space.space_spring.domain.pay.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.CurrentPayRequestState;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.LoadCurrentPayRequestStateUseCase;
import space.space_spring.domain.pay.application.port.in.readPayRequestList.ResultOfReadPayRequestList;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.domain.Bank;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayType;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.DATABASE_ERROR;

class ReadPayRequestListServiceTest {

    private LoadPayRequestPort loadPayRequestPort;
    private LoadCurrentPayRequestStateUseCase loadCurrentPayRequestStateUseCase;
    private ReadPayRequestListService readPayRequestListService;

    private Long seongjunId;

    @BeforeEach
    void setUp() {
        loadPayRequestPort = Mockito.mock(LoadPayRequestPort.class);
        loadCurrentPayRequestStateUseCase = Mockito.mock(LoadCurrentPayRequestStateUseCase.class);
        readPayRequestListService = new ReadPayRequestListService(loadPayRequestPort, loadCurrentPayRequestStateUseCase);

        seongjunId = 1L;
    }

    @Test
    @DisplayName("특정 유저가 생성한 모든 정산의 [payRequestId, 전체 정산 요청 금액, 정산 받은 금액, 전체 정산 대상자 수, 그 중 정산 완료한 사람 수] 정보를 완료된 정산, 진행중인 정산으로 구분해서 반환한다.")
    void readPayRequestList1() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.create(1L, seongjunId, 1L, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest2 = PayRequest.create(2L, seongjunId, 2L, Money.of(20000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.INDIVIDUAL);
        PayRequest payRequest3 = PayRequest.create(3L, seongjunId, 3L, Money.of(20000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.INDIVIDUAL);
        Mockito.when(loadPayRequestPort.loadByPayCreatorId(seongjunId)).thenReturn(List.of(payRequest1, payRequest2, payRequest3));

        Mockito.when(loadCurrentPayRequestStateUseCase.loadCurrentPayRequestState(1L)).thenReturn(CurrentPayRequestState.of(Money.of(3333), NaturalNumber.of(1)));          // payRequest1 의 현재 상황
        Mockito.when(loadCurrentPayRequestStateUseCase.loadCurrentPayRequestState(2L)).thenReturn(CurrentPayRequestState.of(Money.of(0), NaturalNumber.of(0)));             // payRequest2 의 현재 상황
        Mockito.when(loadCurrentPayRequestStateUseCase.loadCurrentPayRequestState(3L)).thenReturn(CurrentPayRequestState.of(Money.of(20000), NaturalNumber.of(2)));         // payRequest3 의 현재 상황

        //when
        ResultOfReadPayRequestList result = readPayRequestListService.readPayRequestList(seongjunId);

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
                        tuple(1L, Money.of(10000), Money.of(3333), NaturalNumber.of(3), NaturalNumber.of(1)),
                        tuple(2L, Money.of(20000), Money.of(0), NaturalNumber.of(2), NaturalNumber.of(0))
                );
    }

    @Test
    @DisplayName("특정 유저가 생성한 정산 중 [현재 진행중인 정산] 이 없을 경우, 현재 진행중인 정산은 빈 ArrayList를 반환한다.")
    void readPayRequestList2() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.create(1L, seongjunId, 1L, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest2 = PayRequest.create(2L, seongjunId, 2L, Money.of(20000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.INDIVIDUAL);
        Mockito.when(loadPayRequestPort.loadByPayCreatorId(seongjunId)).thenReturn(List.of(payRequest1, payRequest2));

        Mockito.when(loadCurrentPayRequestStateUseCase.loadCurrentPayRequestState(1L)).thenReturn(CurrentPayRequestState.of(Money.of(10000), NaturalNumber.of(3)));         // payRequest1 의 현재 상황
        Mockito.when(loadCurrentPayRequestStateUseCase.loadCurrentPayRequestState(2L)).thenReturn(CurrentPayRequestState.of(Money.of(20000), NaturalNumber.of(2)));         // payRequest2 의 현재 상황

        //when
        ResultOfReadPayRequestList result = readPayRequestListService.readPayRequestList(seongjunId);

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
        PayRequest payRequest1 = PayRequest.create(1L, seongjunId, 1L, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest2 = PayRequest.create(2L, seongjunId, 1L, Money.of(20000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.INDIVIDUAL);
        Mockito.when(loadPayRequestPort.loadByPayCreatorId(seongjunId)).thenReturn(List.of(payRequest1, payRequest2));

        Mockito.when(loadCurrentPayRequestStateUseCase.loadCurrentPayRequestState(1L)).thenReturn(CurrentPayRequestState.of(Money.of(3333), NaturalNumber.of(1)));          // payRequest1 의 현재 상황
        Mockito.when(loadCurrentPayRequestStateUseCase.loadCurrentPayRequestState(2L)).thenReturn(CurrentPayRequestState.of(Money.of(10000), NaturalNumber.of(1)));         // payRequest2 의 현재 상황

        //when
        ResultOfReadPayRequestList result = readPayRequestListService.readPayRequestList(seongjunId);

        //then
        assertThat(result.getCompletePayRequestList()).isNotNull().isEmpty();

        assertThat(result.getInCompletePayRequestList()).hasSize(2)
                .extracting(
                        "payRequestId", "totalAmount", "receivedAmount", "totalTargetNum", "sendCompleteTargetNum"
                )
                .containsExactlyInAnyOrder(
                        tuple(1L, Money.of(10000), Money.of(3333), NaturalNumber.of(3), NaturalNumber.of(1)),
                        tuple(2L, Money.of(20000), Money.of(10000), NaturalNumber.of(2), NaturalNumber.of(1))
                );
    }

    @Test
    @DisplayName("특정 멤버가 생성한 정산 요청이 없을 경우, 완료 및 진행중 정산 모두 빈 리스트를 반환한다.")
    void readPayRequestList_emptyList() throws Exception {
        //given
        Mockito.when(loadPayRequestPort.loadByPayCreatorId(seongjunId)).thenReturn(List.of());

        //when
        ResultOfReadPayRequestList result = readPayRequestListService.readPayRequestList(seongjunId);

        //then
        assertThat(result.getCompletePayRequestList()).isEmpty();
        assertThat(result.getInCompletePayRequestList()).isEmpty();
    }

    @Test
    @DisplayName("불일치된 정산 상태인 경우, 데이터 베이스 예외 메시지를 던진다.")
    void readPayRequestList_inconsistentState1() throws Exception {
        //given
        // [송금완료한 금액 == 정산 요청 총 금액 But 송금완료한 사람 != 정산 요청 총 대상 수] 인 경우
        PayRequest payRequest = PayRequest.create(1L, seongjunId, 1L, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        Mockito.when(loadPayRequestPort.loadByPayCreatorId(seongjunId)).thenReturn(List.of(payRequest));
        Mockito.when(loadCurrentPayRequestStateUseCase.loadCurrentPayRequestState(1L))
                .thenReturn(CurrentPayRequestState.of(Money.of(10000), NaturalNumber.of(1)));

        //when //then
        assertThatThrownBy(() -> readPayRequestListService.readPayRequestList(seongjunId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(DATABASE_ERROR.getMessage());
    }

    @Test
    @DisplayName("불일치된 정산 상태인 경우, 데이터 베이스 예외 메시지를 던진다.")
    void readPayRequestList_inconsistentState2() throws Exception {
        //given
        // [송금완료한 금액 != 정산 요청 총 금액 But 송금완료한 사람 == 정산 요청 총 대상 수] 인 경우
        PayRequest payRequest = PayRequest.create(1L, seongjunId, 1L, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        Mockito.when(loadPayRequestPort.loadByPayCreatorId(seongjunId)).thenReturn(List.of(payRequest));
        Mockito.when(loadCurrentPayRequestStateUseCase.loadCurrentPayRequestState(1L))
                .thenReturn(CurrentPayRequestState.of(Money.of(5000), NaturalNumber.of(3)));

        //when //then
        assertThatThrownBy(() -> readPayRequestListService.readPayRequestList(seongjunId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(DATABASE_ERROR.getMessage());
    }
}