package space.space_spring.domain.pay.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.application.port.out.UpdatePayPort;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.global.common.entity.BaseInfo;
import space.space_spring.global.exception.CustomException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.PAY_REQUEST_TARGET_MISMATCH;

class CompletePayServiceTest {

    private UpdatePayPort updatePayPort;
    private LoadPayRequestTargetPort loadPayRequestTargetPort;
    private CompletePayService completePayService;

    private Long seongjunId;
    private Long sangjunId;
    private Long seohyunId;
    private Long kyungminId;

    @BeforeEach
    void setUp() {
        updatePayPort = Mockito.mock(UpdatePayPort.class);
        loadPayRequestTargetPort = Mockito.mock(LoadPayRequestTargetPort.class);
        completePayService = new CompletePayService(updatePayPort, loadPayRequestTargetPort);

        seongjunId = 1L;
        sangjunId = 2L;
        seohyunId = 3L;
        kyungminId = 4L;
    }

    @Test
    @DisplayName("정산 타겟 멤버는 본인이 요청받은 특정 정산 요청에 대해 완료처리를 할 수 있다.")
    void completeForRequestedPay1() throws Exception {
        //given
        Long targetMemberId = seongjunId;
        Long payRequestTargetId = 1L;

        PayRequestTarget payRequestTarget = PayRequestTarget.of(
                payRequestTargetId, targetMemberId, 1L, Money.of(10000), false, BaseInfo.ofEmpty()
        );

        when(loadPayRequestTargetPort.loadById(payRequestTargetId)).thenReturn(payRequestTarget);

        //when
        completePayService.completeForRequestedPay(targetMemberId, payRequestTargetId);

        //then
        assertThat(payRequestTarget.isComplete()).isTrue();
    }

    @Test
    @DisplayName("본인이 아닌 다른 사람의 정산 요청에 대해 완료처리를 요청할 경우, 에러를 발생시킨다.")
    void completeForRequestedPay2() throws Exception {
        //given
        Long targetMemberId = seongjunId;
        Long payRequestTargetId = 1L;

        PayRequestTarget payRequestTarget = PayRequestTarget.of(
                payRequestTargetId, targetMemberId, 1L, Money.of(10000), false, BaseInfo.ofEmpty()
        );

        when(loadPayRequestTargetPort.loadById(payRequestTargetId)).thenReturn(payRequestTarget);

        //when //then
        // 경민이가 payRequestTarget에 대해서 정산 완료 처리를 요청
        assertThatThrownBy(() -> completePayService.completeForRequestedPay(kyungminId, payRequestTargetId))
                .isInstanceOf(CustomException.class)
                .hasMessage(PAY_REQUEST_TARGET_MISMATCH.getMessage());
    }

}