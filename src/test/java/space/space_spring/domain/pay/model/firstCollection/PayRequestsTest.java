package space.space_spring.domain.pay.model.firstCollection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space.space_spring.domain.pay.model.entity.PayRequest;
import space.space_spring.domain.pay.model.entity.PayRequestTarget;
import space.space_spring.domain.space.model.entity.Space;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.entity.enumStatus.UserSignupType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class PayRequestsTest {

    private User seongjun;
    private User sangjun;
    private User seohyun;
    private User kyeongmin;
    private Space kuit;

    @BeforeEach
    void setUp() {
        // 1L : seongjun, 2L : sangjun, 3L : seohyun, 4L : kyeongmin
        seongjun = User.create("email", "password", "노성준", UserSignupType.LOCAL);
        sangjun = User.create("email", "password", "김상준", UserSignupType.LOCAL);
        seohyun = User.create("email", "password", "정서현", UserSignupType.LOCAL);
        kyeongmin = User.create("email", "password", "김경민", UserSignupType.LOCAL);
        kuit = Space.create("space", "profileImg");
    }

    @Test
    @DisplayName("멤버 변수인 PayRequest list 로부터 [payRequestId, 정산 총 금액, 현재까지 받은 금액, 정산 요청한 총 사람 수, 그 중 돈을 보낸 사람 수] 정보를 찾아준다.")
    void getPayRequestInfos() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.create(seongjun, kuit, 40000, "우리은행", "111-111");
        PayRequestTarget target1 = PayRequestTarget.create(payRequest1, 2L, 20000);
        PayRequestTarget target2 = PayRequestTarget.create(payRequest1, 3L, 20000);

        PayRequest payRequest2 = PayRequest.create(seongjun, kuit, 40000, "신한은행", "222-222");
        PayRequestTarget target3 = PayRequestTarget.create(payRequest2, 3L, 10000);
        PayRequestTarget target4 = PayRequestTarget.create(payRequest2, 4L, 30000);

        target1.changeCompleteStatus(true);         // payRequest1 에 대해서 target1은 정산 완료
        payRequest1.receiveMoneyFromTarget(20000);

        target3.changeCompleteStatus(true);         // payRequest2 에 대해서 target3은 정산 완료
        payRequest2.receiveMoneyFromTarget(10000);

        PayRequests payCreatorIsSeongjun = PayRequests.create(List.of(payRequest1, payRequest2));

        //when
        PayRequestInfos payRequestInfos = payCreatorIsSeongjun.getPayRequestInfos();

        //then
        assertThat(payRequestInfos.getAll()).hasSize(2)
                .extracting("totalAmount", "receiveAmount", "totalTargetNum", "paySendTargetNum")
                .containsExactlyInAnyOrder(
                        tuple(40000, 20000, 2, 1),
                        tuple(40000, 10000, 2, 1)
                );
    }

}