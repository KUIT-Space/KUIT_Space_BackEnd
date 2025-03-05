package space.space_spring.domain.pay.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.pay.application.port.out.DeletePayRequestPort;
import space.space_spring.domain.pay.application.port.out.DeletePayRequestTargetPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.domain.*;
import space.space_spring.global.common.entity.BaseInfo;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.PAY_REQUEST_CREATOR_MISMATCH;

class DeletePayServiceTest {

    private LoadPayRequestPort loadPayRequestPort;
    private LoadPayRequestTargetPort loadPayRequestTargetPort;
    private DeletePayRequestPort deletePayRequestPort;
    private DeletePayRequestTargetPort deletePayRequestTargetPort;
    private DeletePayService deletePayService;

    private Long seongjunId;
    private Long sangjunId;
    private Long seohyunId;
    private Long kyungminId;

    @BeforeEach
    void setUp() {
        loadPayRequestPort = Mockito.mock(LoadPayRequestPort.class);
        loadPayRequestTargetPort = Mockito.mock(LoadPayRequestTargetPort.class);
        deletePayRequestPort = Mockito.mock(DeletePayRequestPort.class);
        deletePayRequestTargetPort = Mockito.mock(DeletePayRequestTargetPort.class);
        deletePayService = new DeletePayService(loadPayRequestPort, loadPayRequestTargetPort, deletePayRequestPort, deletePayRequestTargetPort);

        seongjunId = 1L;
        sangjunId = 2L;
        seohyunId = 3L;
        kyungminId = 4L;
    }

    @Test
    @DisplayName("스페이스 멤버가 생성한 정산 요청, 모든 정산 요청 타겟의 status를 inactive로 변경한다(= soft delete 처리한다).")
    void deletePay1() throws Exception {
        //given
        PayRequest payRequest = PayRequest.create(1L, seongjunId, 1L, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT, BaseInfo.ofEmpty());
        when(loadPayRequestPort.loadById(1L)).thenReturn(payRequest);

        PayRequestTarget target1 = PayRequestTarget.create(1L, sangjunId, 1L, Money.of(3333), BaseInfo.ofEmpty());
        PayRequestTarget target2 = PayRequestTarget.create(2L, seohyunId, 1L, Money.of(3333), BaseInfo.ofEmpty());
        PayRequestTarget target3 = PayRequestTarget.create(3L, kyungminId, 1L, Money.of(3333), BaseInfo.ofEmpty());
        when(loadPayRequestTargetPort.loadByPayRequestId(1L)).thenReturn(List.of(target1, target2, target3));

        //when
        deletePayService.deletePay(seongjunId, 1L);

        //then
        List<Long> expectedTargetIds = List.of(target1.getId(), target2.getId(), target3.getId());

        // deleteAllPayRequestTarget 메서드가 정확한 인자값으로 한 번 호출되었는지 검증
        Mockito.verify(deletePayRequestTargetPort, Mockito.times(1)).deleteAllPayRequestTarget(expectedTargetIds);

        // deletePayRequest 메서드가 payRequestId(= 1L)로 한 번 호출되었는지 검증
        Mockito.verify(deletePayRequestPort, Mockito.times(1)).deletePayRequest(1L);
    }

    @Test
    @DisplayName("본인이 생성하지 않은 정산에 대해 정산 삭제 요청이 들어올 경우, 예외를 발생시킨다.")
    void deletePay2() throws Exception {
        //given
        PayRequest payRequest = PayRequest.create(1L, seongjunId, 1L, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT, BaseInfo.ofEmpty());
        when(loadPayRequestPort.loadById(1L)).thenReturn(payRequest);       // 정산 생성자는 seongjun

        //when //then
        assertThatThrownBy(() -> deletePayService.deletePay(kyungminId, 1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(PAY_REQUEST_CREATOR_MISMATCH.getMessage());
    }
}