package space.space_spring.domain.pay.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space.space_spring.global.common.entity.BaseInfo;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PayRequestTargetsTest {

    private Long seongjunId;
    private Long sangjunId;
    private Long seohyunId;
    private Long kyungminId;

    @BeforeEach
    void setUp() {
        seongjunId = 1L;
        sangjunId = 2L;
        seohyunId = 3L;
        kyungminId = 4L;
    }

    @Test
    @DisplayName("List<PayRequestTarget> 중 현재 송금을 완료한 사람의 수를 계산한다.")
    void calculateNumberOfSendCompleteTarget1() throws Exception {
        //given
        PayRequestTarget target1 = PayRequestTarget.of(1L, seongjunId, 1L, Money.of(10000), true, BaseInfo.ofEmpty());
        PayRequestTarget target2 = PayRequestTarget.create(2L, sangjunId, 1L, Money.of(10000), BaseInfo.ofEmpty());
        PayRequestTarget target3 = PayRequestTarget.create(3L, seohyunId, 1L, Money.of(10000), BaseInfo.ofEmpty());
        PayRequestTarget target4 = PayRequestTarget.create(4L, kyungminId, 1L, Money.of(10000), BaseInfo.ofEmpty());
        PayRequestTargets payRequestTargets = PayRequestTargets.create(List.of(target1, target2, target3, target4));

        //when
        NaturalNumber number = payRequestTargets.calculateNumberOfSendCompleteTarget();

        //then
        assertThat(number).isEqualTo(NaturalNumber.of(1));
    }

    @Test
    @DisplayName("List<PayRequestTarget> 중 현재 송금을 완료한 사람이 없을 경우, 0을 반환한다.")
    void calculateNumberOfSendCompleteTarget2() throws Exception {
        //given
        PayRequestTarget target1 = PayRequestTarget.create(1L, seongjunId, 1L, Money.of(10000), BaseInfo.ofEmpty());
        PayRequestTarget target2 = PayRequestTarget.create(2L, sangjunId, 1L, Money.of(10000), BaseInfo.ofEmpty());
        PayRequestTarget target3 = PayRequestTarget.create(3L, seohyunId, 1L, Money.of(10000), BaseInfo.ofEmpty());
        PayRequestTarget target4 = PayRequestTarget.create(4L, kyungminId, 1L, Money.of(10000), BaseInfo.ofEmpty());
        PayRequestTargets payRequestTargets = PayRequestTargets.create(List.of(target1, target2, target3, target4));

        //when
        NaturalNumber number = payRequestTargets.calculateNumberOfSendCompleteTarget();

        //then
        assertThat(number).isEqualTo(NaturalNumber.of(0));
    }

    @Test
    @DisplayName("List<PayRequestTarget> 중 현재 송금 완료한 금액의 합을 계산한다.")
    void calculateMoneyOfSendComplete1() throws Exception {
        //given
        PayRequestTarget target1 = PayRequestTarget.create(1L, seongjunId, 1L, Money.of(10000), BaseInfo.ofEmpty());
        PayRequestTarget target2 = PayRequestTarget.of(2L, sangjunId, 1L, Money.of(20000), true, BaseInfo.ofEmpty());
        PayRequestTarget target3 = PayRequestTarget.of(3L, seohyunId, 1L, Money.of(30000), true, BaseInfo.ofEmpty());
        PayRequestTarget target4 = PayRequestTarget.create(4L, kyungminId, 1L, Money.of(10000), BaseInfo.ofEmpty());
        PayRequestTargets payRequestTargets = PayRequestTargets.create(List.of(target1, target2, target3, target4));

        //when
        Money money = payRequestTargets.calculateMoneyOfSendComplete();

        //then
        assertThat(money).isEqualTo(Money.of(50000));
    }

    @Test
    @DisplayName("List<PayRequestTarget> 중 현재 송금 완료한 사람이 없을 경우, 0원을 반환한다.")
    void calculateMoneyOfSendComplete2() throws Exception {
        //given
        PayRequestTarget target1 = PayRequestTarget.create(1L, seongjunId, 1L, Money.of(10000), BaseInfo.ofEmpty());
        PayRequestTarget target2 = PayRequestTarget.create(2L, sangjunId, 1L, Money.of(20000), BaseInfo.ofEmpty());
        PayRequestTarget target3 = PayRequestTarget.create(3L, seohyunId, 1L, Money.of(30000), BaseInfo.ofEmpty());
        PayRequestTarget target4 = PayRequestTarget.create(4L, kyungminId, 1L, Money.of(10000), BaseInfo.ofEmpty());
        PayRequestTargets payRequestTargets = PayRequestTargets.create(List.of(target1, target2, target3, target4));

        //when
        Money money = payRequestTargets.calculateMoneyOfSendComplete();

        //then
        assertThat(money).isEqualTo(Money.of(0));
    }
}