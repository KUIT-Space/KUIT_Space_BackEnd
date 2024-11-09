package space.space_spring.domain.pay.model.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import space.space_spring.domain.pay.model.dto.PayRequestInfoDto;
import space.space_spring.domain.space.model.entity.Space;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.entity.enumStatus.UserSignupType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class PayRequestTest {

    @Test
    @DisplayName("해당 PayRequest의 target list를 인자로 받으면, PayRequestInfoDto를 return 한다.")
    void createPayRequestInfo() throws Exception {
        //given
        User payCreator = User.create("email", "password", "노성준", UserSignupType.LOCAL);

        Space space = Space.create("space", "img");

        PayRequest payRequest = PayRequest.create(payCreator, space, 30000, "우리은행", "111-111");

        PayRequestTarget target1 = PayRequestTarget.create(payRequest, 1L, 10000);
        PayRequestTarget target2 = PayRequestTarget.create(payRequest, 2L, 10000);
        PayRequestTarget target3 = PayRequestTarget.create(payRequest, 3L, 10000);

        // target1 : 미정산, target2, target3 : 정산 완료
        target2.changeCompleteStatus(true);
        target3.changeCompleteStatus(true);
        payRequest.changeReceiveAmount(20000);

        List<PayRequestTarget> targets = List.of(target1, target2, target3);

        //when
        PayRequestInfoDto payRequestInfo = payRequest.createPayRequestInfo(targets);

        //then
        assertThat(payRequestInfo)
                .extracting("totalAmount", "receiveAmount", "totalTargetNum", "paySendTargetNum")
                .contains(30000, 20000, 3, 2);
    }

}