package space.space_spring.domain.pay.model.firstCollection;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space.space_spring.domain.pay.model.entity.PayRequest;
import space.space_spring.domain.pay.model.entity.PayRequestTarget;
import space.space_spring.domain.space.model.entity.Space;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.entity.enumStatus.UserSignupType;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PayRequestTargetsTest {

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
    @DisplayName("멤버 변수인 PayRequestTarget list 로부터 [payRequestTargetId, 정산 생성자 이름, 요청받은 정산 금액, 송금해야할 은행 이름, 계좌번호] 정보를 찾아준다.")
    void getPayTargetInfos() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.create(sangjun, kuit, 40000, "우리은행", "111-111");
        PayRequestTarget target1 = PayRequestTarget.create(payRequest1, 1L, 20000);
        PayRequestTarget target2 = PayRequestTarget.create(payRequest1, 2L, 20000);

        PayRequest payRequest2 = PayRequest.create(seohyun, kuit, 40000, "신한은행", "222-222");
        PayRequestTarget target3 = PayRequestTarget.create(payRequest2, 1L, 10000);
        PayRequestTarget target4 = PayRequestTarget.create(payRequest2, 4L, 30000);

        PayRequestTargets targetIsSeongjun = PayRequestTargets.create(List.of(target1, target3));

        //when
        PayTargetInfos payTargetInfos = targetIsSeongjun.getPayTargetInfos();

        //then
        assertThat(payTargetInfos.getAll()).hasSize(2)
                .extracting("payCreatorName", "requestedAmount", "bankName", "bankAccountNum")
                .containsExactlyInAnyOrder(
                        tuple("김상준", 20000, "우리은행", "111-111"),
                        tuple("정서현", 10000, "신한은행", "222-222")
                );
    }

    @Test
    @DisplayName("멤버 변수인 PayRequestTarget list 의 전체 크기를 알려준다.")
    void countTotalTargets() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.create(sangjun, kuit, 40000, "우리은행", "111-111");
        PayRequestTarget target1 = PayRequestTarget.create(payRequest1, 1L, 20000);
        PayRequestTarget target2 = PayRequestTarget.create(payRequest1, 2L, 20000);

        PayRequest payRequest2 = PayRequest.create(seohyun, kuit, 40000, "신한은행", "222-222");
        PayRequestTarget target3 = PayRequestTarget.create(payRequest2, 1L, 10000);
        PayRequestTarget target4 = PayRequestTarget.create(payRequest2, 4L, 30000);

        PayRequestTargets targetIsSeongjun = PayRequestTargets.create(List.of(target1, target3));

        //when
        int totalCount = targetIsSeongjun.countTotalTargets();

        //then
        assertThat(totalCount).isEqualTo(2);
    }

    @Test
    @DisplayName("멤버 변수인 PayRequestTarget list 중 정산을 완료한 타겟이 몇 명인지 알려준다.")
    void countCompleteTargets() throws Exception {
        //given
        PayRequest payRequest1 = PayRequest.create(sangjun, kuit, 40000, "우리은행", "111-111");
        PayRequestTarget target1 = PayRequestTarget.create(payRequest1, 1L, 20000);
        PayRequestTarget target2 = PayRequestTarget.create(payRequest1, 2L, 20000);

        PayRequest payRequest2 = PayRequest.create(seohyun, kuit, 40000, "신한은행", "222-222");
        PayRequestTarget target3 = PayRequestTarget.create(payRequest2, 1L, 10000);
        PayRequestTarget target4 = PayRequestTarget.create(payRequest2, 4L, 30000);

        PayRequestTargets targetIsSeongjun = PayRequestTargets.create(List.of(target1, target3));

        // sangjun이 요청한 정산은 정산 완료 & seohyun이 요청한 정산은 정산 완료 X
        target1.changeCompleteStatus(true);

        //when
        int completeCount = targetIsSeongjun.countCompleteTargets();

        //then
        assertThat(completeCount).isEqualTo(1);
    }



}