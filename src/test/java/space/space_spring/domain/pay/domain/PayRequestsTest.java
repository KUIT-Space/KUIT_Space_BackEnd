package space.space_spring.domain.pay.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space.space_spring.domain.space.domain.Space;
import space.space_spring.domain.spaceMember.SpaceMember;
import space.space_spring.domain.user.domain.User;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class PayRequestsTest {

    private Space kuit;
    private SpaceMember seongjun;
    private SpaceMember sangjun;
    private SpaceMember seohyun;
    private SpaceMember kyeongmin;

    @BeforeEach
    void setUp() {
        User commonUser = User.create(1L, 1L);          // User 도메인 엔티티는 그냥 하나로 공유해서 테스트 진행
        kuit = Space.create(1L, "쿠잇", 1L);

        seongjun = SpaceMember.create(1L, kuit, commonUser, 1L, "노성준", "image_111", true);
        sangjun = SpaceMember.create(2L, kuit, commonUser, 2L, "개구리비안", "image_222", false);
        seohyun = SpaceMember.create(3L, kuit, commonUser, 3L, "정서현", "image_333", false);
        kyeongmin = SpaceMember.create(4L, kuit, commonUser, 4L, "김경민", "image_444", false);
    }

    @Test
    @DisplayName("List<PayRequest> 중 완료된 정산들을 반환한다.")
    void getCompletePayRequestList1() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.createNewPayRequest(1L, seongjun, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest2 = PayRequest.createNewPayRequest(2L, sangjun, Money.of(20000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest3 = PayRequest.createNewPayRequest(3L, seohyun, Money.of(30000), NaturalNumber.of(5), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest4 = PayRequest.createNewPayRequest(4L, kyeongmin, Money.of(1000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        payRequest1.changeToComplete();
        payRequest2.changeToComplete();
        payRequest3.changeToComplete();

        PayRequests payRequests = PayRequests.create(List.of(payRequest1, payRequest2, payRequest3, payRequest4));

        //when
        List<PayRequest> completePayRequestList = payRequests.getCompletePayRequestList();

        //then
        assertThat(completePayRequestList).hasSize(3)
                .extracting("payCreator", "totalAmount", "totalTargetNum")
                .containsExactlyInAnyOrder(
                        tuple(seongjun, Money.of(10000), NaturalNumber.of(3)),
                        tuple(sangjun, Money.of(20000), NaturalNumber.of(2)),
                        tuple(seohyun, Money.of(30000), NaturalNumber.of(5))
                );
    }

    @Test
    @DisplayName("List<PayRequest> 중 완료된 정산이 없는 경우, 빈 ArrayList를 반환한다.")
    void getCompletePayRequestList2() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.createNewPayRequest(1L, seongjun, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest2 = PayRequest.createNewPayRequest(2L, sangjun, Money.of(20000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest3 = PayRequest.createNewPayRequest(3L, seohyun, Money.of(30000), NaturalNumber.of(5), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest4 = PayRequest.createNewPayRequest(4L, kyeongmin, Money.of(1000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequests payRequests = PayRequests.create(List.of(payRequest1, payRequest2, payRequest3, payRequest4));

        //when
        List<PayRequest> completePayRequestList = payRequests.getCompletePayRequestList();

        //then
        assertThat(completePayRequestList).isEmpty();
    }

    @Test
    @DisplayName("List<PayRequest> 중 현재 진행 중인 정산들을 반환한다.")
    void getInCompletePayRequestList1() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.createNewPayRequest(1L, seongjun, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest2 = PayRequest.createNewPayRequest(2L, sangjun, Money.of(20000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest3 = PayRequest.createNewPayRequest(3L, seohyun, Money.of(30000), NaturalNumber.of(5), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest4 = PayRequest.createNewPayRequest(4L, kyeongmin, Money.of(1000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        payRequest1.changeToComplete();
        payRequest2.changeToComplete();
        payRequest3.changeToComplete();

        PayRequests payRequests = PayRequests.create(List.of(payRequest1, payRequest2, payRequest3, payRequest4));

        //when
        List<PayRequest> inCompletePayRequestList = payRequests.getInCompletePayRequestList();

        //then
        assertThat(inCompletePayRequestList).hasSize(1)
                .extracting("payCreator", "totalAmount", "totalTargetNum")
                .containsExactlyInAnyOrder(
                        tuple(kyeongmin, Money.of(1000), NaturalNumber.of(2))
                );
    }

    @Test
    @DisplayName("List<PayRequest> 중 현재 진행 중인 정산이 없는 경우, 빈 ArrayList를 반환한다.")
    void getInCompletePayRequestList2() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.createNewPayRequest(1L, seongjun, Money.of(10000), NaturalNumber.of(3), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest2 = PayRequest.createNewPayRequest(2L, sangjun, Money.of(20000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest3 = PayRequest.createNewPayRequest(3L, seohyun, Money.of(30000), NaturalNumber.of(5), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        PayRequest payRequest4 = PayRequest.createNewPayRequest(4L, kyeongmin, Money.of(1000), NaturalNumber.of(2), Bank.of("우리은행", "111-111"), PayType.EQUAL_SPLIT);
        payRequest1.changeToComplete();
        payRequest2.changeToComplete();
        payRequest3.changeToComplete();
        payRequest4.changeToComplete();

        PayRequests payRequests = PayRequests.create(List.of(payRequest1, payRequest2, payRequest3, payRequest4));

        //when
        List<PayRequest> inCompletePayRequestList = payRequests.getInCompletePayRequestList();

        //then
        assertThat(inCompletePayRequestList).isEmpty();
    }
}