package space.space_spring.domain.pay.application.service;

import org.hibernate.grammars.hql.HqlParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.pay.application.port.in.loadCurrentPayRequestState.CurrentPayRequestState;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.NaturalNumber;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.THIS_PAY_REQUEST_HAS_NOT_TARGETS;

class LoadCurrentPayRequestStateServiceTest {

    private LoadPayRequestTargetPort loadPayRequestTargetPort;
    private LoadCurrentPayRequestStateService loadCurrentPayRequestStateService;

    private Long seongjunId;
    private Long sangjunId;
    private Long seohyunId;
    private Long kyungminId;

    @BeforeEach
    void setUp() {
        loadPayRequestTargetPort = Mockito.mock(LoadPayRequestTargetPort.class);
        loadCurrentPayRequestStateService = new LoadCurrentPayRequestStateService(loadPayRequestTargetPort);

        seongjunId = 1L;
        sangjunId = 2L;
        seohyunId = 3L;
        kyungminId = 4L;
    }

    @Test
    @DisplayName("payRequestId 를 받아 해당 정산 요청의 [현재까지 송금 받은 금액, 현재까지 송금한 사람 수] 정보를 반환한다.")
    void loadCurrentPayRequestState1() throws Exception {
        //given
        Long payRequestId = 1L;
        PayRequestTarget target1 = PayRequestTarget.create(1L, seongjunId, payRequestId, Money.of(2000));
        PayRequestTarget target2 = PayRequestTarget.create(2L, sangjunId, payRequestId, Money.of(3000));
        PayRequestTarget target3 = PayRequestTarget.of(3L, seohyunId, payRequestId, Money.of(4000), true);
        PayRequestTarget target4 = PayRequestTarget.of(4L, kyungminId, payRequestId, Money.of(8000), true);
        List<PayRequestTarget> targetList = List.of(target1, target2, target3, target4);

        Mockito.when(loadPayRequestTargetPort.loadByPayRequestId(payRequestId)).thenReturn(targetList);

        //when
        CurrentPayRequestState currentState = loadCurrentPayRequestStateService.loadCurrentPayRequestState(payRequestId);

        //then
        assertThat(currentState.getReceivedAmount()).isEqualTo(Money.of(12000));
        assertThat(currentState.getSendCompleteTargetNum()).isEqualTo(NaturalNumber.of(2));
    }

    @Test
    @DisplayName("payRequestId 로 찾아온 정산 요청에 정산 대상이 없으면 예외를 발생시킨다.")
    void loadCurrentPayRequestState2() throws Exception {
        //given
        Long payRequestId = 1L;
        Mockito.when(loadPayRequestTargetPort.loadByPayRequestId(payRequestId)).thenReturn(Collections.emptyList());

        //when //then
        assertThatThrownBy(() -> loadCurrentPayRequestStateService.loadCurrentPayRequestState(payRequestId))
                .isInstanceOf(CustomException.class)
                .hasMessage(THIS_PAY_REQUEST_HAS_NOT_TARGETS.getMessage());
    }
}