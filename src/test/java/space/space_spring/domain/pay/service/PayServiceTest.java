package space.space_spring.domain.pay.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import space.space_spring.domain.pay.model.dto.PayTargetInfoDto;
import space.space_spring.domain.space.repository.SpaceRepository;
import space.space_spring.domain.userSpace.repository.UserSpaceRepository;
import space.space_spring.domain.pay.model.dto.PayRequestInfoDto;
import space.space_spring.domain.pay.model.entity.PayRequest;
import space.space_spring.domain.pay.model.entity.PayRequestTarget;
import space.space_spring.domain.pay.model.response.PayHomeViewResponse;
import space.space_spring.domain.pay.repository.PayRequestRepository;
import space.space_spring.domain.pay.repository.PayRequestTargetRepository;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.domain.user.repository.UserRepository;
import space.space_spring.domain.space.model.entity.Space;
import space.space_spring.domain.userSpace.model.entity.UserSpace;
import space.space_spring.entity.enumStatus.UserSignupType;
import space.space_spring.entity.enumStatus.UserSpaceAuth;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@DataJpaTest
@Import({PayService.class})
@ActiveProfiles("test")
@EnableJpaRepositories(basePackageClasses = {PayRequestRepository.class, PayRequestTargetRepository.class, UserRepository.class, SpaceRepository.class, UserSpaceRepository.class})
@EntityScan(basePackageClasses = {PayRequest.class, PayRequestTarget.class, User.class, Space.class, UserSpace.class})
class PayServiceTest {

    @Autowired
    private PayService payService;

    @Autowired
    private PayRequestRepository payRequestRepository;

    @Autowired
    private PayRequestTargetRepository payRequestTargetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private UserSpaceRepository userSpaceRepository;

    @Test
    @DisplayName("유저가 요청한 정산들 중, 완료되지 않은 정산 정보들을 return 한다.")
    void getPayHomeInfos1() throws Exception {
        //given
        User user1 = User.create("email", "password", "name", UserSignupType.LOCAL);
        User user2 = User.create("email", "password", "name", UserSignupType.LOCAL);
        User user3 = User.create("email", "password", "name", UserSignupType.LOCAL);
        User user4 = User.create("email", "password", "name", UserSignupType.LOCAL);
        Space space = Space.create("space", "profileImg");

        User payCreator = userRepository.save(user1);
        User payTarget1 = userRepository.save(user2);
        User payTarget2 = userRepository.save(user3);
        User payTarget3 = userRepository.save(user4);
        Space testSpace = spaceRepository.save(space);

        UserSpace userSpace = UserSpace.create(payCreator, testSpace, UserSpaceAuth.NORMAL);
        userSpaceRepository.save(userSpace);

        PayRequest inCompletePay1 = PayRequest.create(payCreator, testSpace, 20000, "bank", "accountNum");
        PayRequest inCompletePay2 = PayRequest.create(payCreator, testSpace, 40000, "bank", "accountNum");
        PayRequest completePay = PayRequest.create(payCreator, testSpace, 20000, "bank", "accountNum");
        completePay.changeCompleteStatus(true);

        PayRequestTarget inCompleteTarget1 = PayRequestTarget.create(inCompletePay1, payTarget1.getUserId(), 10000);
        PayRequestTarget inCompleteTarget2 = PayRequestTarget.create(inCompletePay1, payTarget2.getUserId(), 10000);

        PayRequestTarget inCompleteTarget3 = PayRequestTarget.create(inCompletePay2, payTarget2.getUserId(), 20000);
        PayRequestTarget inCompleteTarget4 = PayRequestTarget.create(inCompletePay2, payTarget3.getUserId(), 20000);

        PayRequestTarget completeTarget1 = PayRequestTarget.create(completePay, payTarget1.getUserId(), 10000);
        PayRequestTarget completeTarget2 = PayRequestTarget.create(completePay, payTarget2.getUserId(), 10000);
        completeTarget1.changeCompleteStatus(true);
        completeTarget2.changeCompleteStatus(true);

        payRequestRepository.save(inCompletePay1);
        payRequestRepository.save(inCompletePay2);
        payRequestRepository.save(completePay);

        payRequestTargetRepository.save(inCompleteTarget1);
        payRequestTargetRepository.save(inCompleteTarget2);
        payRequestTargetRepository.save(inCompleteTarget3);
        payRequestTargetRepository.save(inCompleteTarget4);
        payRequestTargetRepository.save(completeTarget1);
        payRequestTargetRepository.save(completeTarget2);

        //when
        PayHomeViewResponse payHomeInfos = payService.getPayHomeInfos(payCreator.getUserId(), testSpace.getSpaceId());
        List<PayRequestInfoDto> payRequestInfos = payHomeInfos.getPayRequestInfoDtos();

        //then
        assertThat(payRequestInfos).hasSize(2)
                .extracting("payRequestId", "totalAmount", "receiveAmount", "totalTargetNum", "paySendTargetNum")
                .containsExactlyInAnyOrder(
                        tuple(inCompletePay1.getPayRequestId(), 20000, 0, 2, 0),
                        tuple(inCompletePay2.getPayRequestId(), 40000, 0, 2, 0)
                );
    }

    @Test
    @DisplayName("유저가 요청받은 정산들 중, 완료되지 않은 정산 정보들을 return 한다.")
    void getPayHomeInfos2() throws Exception {
        //given
        User user1 = User.create("email", "password", "노성준", UserSignupType.LOCAL);
        User user2 = User.create("email", "password", "name", UserSignupType.LOCAL);
        User user3 = User.create("email", "password", "name", UserSignupType.LOCAL);
        User user4 = User.create("email", "password", "name", UserSignupType.LOCAL);
        Space space = Space.create("space", "profileImg");

        User payCreator = userRepository.save(user1);
        User payTarget1 = userRepository.save(user2);
        User payTarget2 = userRepository.save(user3);
        User payTarget3 = userRepository.save(user4);
        Space testSpace = spaceRepository.save(space);

        UserSpace userSpace = UserSpace.create(payTarget2, testSpace, UserSpaceAuth.NORMAL);
        userSpaceRepository.save(userSpace);

        PayRequest inCompletePay1 = PayRequest.create(payCreator, testSpace, 20000, "우리은행", "111-111");
        PayRequest inCompletePay2 = PayRequest.create(payCreator, testSpace, 40000, "국민은행", "222-222");
        PayRequest completePay = PayRequest.create(payCreator, testSpace, 20000, "bank", "accountNum");
        completePay.changeCompleteStatus(true);

        PayRequestTarget inCompleteTarget1 = PayRequestTarget.create(inCompletePay1, payTarget1.getUserId(), 10000);
        PayRequestTarget inCompleteTarget2 = PayRequestTarget.create(inCompletePay1, payTarget2.getUserId(), 10000);

        PayRequestTarget inCompleteTarget3 = PayRequestTarget.create(inCompletePay2, payTarget2.getUserId(), 20000);
        PayRequestTarget inCompleteTarget4 = PayRequestTarget.create(inCompletePay2, payTarget3.getUserId(), 20000);

        PayRequestTarget completeTarget1 = PayRequestTarget.create(completePay, payTarget1.getUserId(), 10000);
        PayRequestTarget completeTarget2 = PayRequestTarget.create(completePay, payTarget2.getUserId(), 10000);
        completeTarget1.changeCompleteStatus(true);
        completeTarget2.changeCompleteStatus(true);

        payRequestRepository.save(inCompletePay1);
        payRequestRepository.save(inCompletePay2);
        payRequestRepository.save(completePay);

        payRequestTargetRepository.save(inCompleteTarget1);
        payRequestTargetRepository.save(inCompleteTarget2);
        payRequestTargetRepository.save(inCompleteTarget3);
        payRequestTargetRepository.save(inCompleteTarget4);
        payRequestTargetRepository.save(completeTarget1);
        payRequestTargetRepository.save(completeTarget2);

        //when
        PayHomeViewResponse payHomeInfos = payService.getPayHomeInfos(payTarget2.getUserId(), testSpace.getSpaceId());
        List<PayTargetInfoDto> payReceiveInfoDtoList = payHomeInfos.getPayTargetInfoDtos();

        //then
        assertThat(payReceiveInfoDtoList).hasSize(2)
                .extracting("payRequestTargetId", "payCreatorName", "requestedAmount", "bankName", "bankAccountNum")
                .containsExactlyInAnyOrder(
                        tuple(inCompleteTarget2.getPayRequestTargetId(), "노성준", 10000, "우리은행", "111-111"),
                        tuple(inCompleteTarget3.getPayRequestTargetId(), "노성준", 20000, "국민은행", "222-222")
                );
    }

    @Test
    @DisplayName("유저가 요청한 정산들 중, 완료되지 않은 정산이 없으면 빈 ArrayList 를 return 한다.")
    void getPayHomeInfos3() throws Exception {
        //given
        User user1 = User.create("email", "password", "노성준", UserSignupType.LOCAL);
        User user2 = User.create("email", "password", "name", UserSignupType.LOCAL);
        User user3 = User.create("email", "password", "name", UserSignupType.LOCAL);
        Space space = Space.create("space", "profileImg");

        User payCreator = userRepository.save(user1);
        User payTarget1 = userRepository.save(user2);
        User payTarget2 = userRepository.save(user3);
        Space testSpace = spaceRepository.save(space);

        UserSpace userSpace = UserSpace.create(payCreator, testSpace, UserSpaceAuth.NORMAL);
        userSpaceRepository.save(userSpace);

        PayRequest completePay = PayRequest.create(payCreator, testSpace, 20000, "bank", "accountNum");
        completePay.changeCompleteStatus(true);

        PayRequestTarget completeTarget1 = PayRequestTarget.create(completePay, payTarget1.getUserId(), 10000);
        PayRequestTarget completeTarget2 = PayRequestTarget.create(completePay, payTarget2.getUserId(), 10000);
        completeTarget1.changeCompleteStatus(true);
        completeTarget2.changeCompleteStatus(true);

        payRequestRepository.save(completePay);

        payRequestTargetRepository.save(completeTarget1);
        payRequestTargetRepository.save(completeTarget2);

        //when
        PayHomeViewResponse payHomeInfos = payService.getPayHomeInfos(payCreator.getUserId(), testSpace.getSpaceId());
        List<PayRequestInfoDto> payRequestInfoDtoList = payHomeInfos.getPayRequestInfoDtos();

        //then
        assertThat(payRequestInfoDtoList).hasSize(0);
    }

    @Test
    @DisplayName("유저가 요청받은 정산들 중, 완료되지 않은 정산이 없으면 빈 ArrayList를 return 한다.")
    void test() throws Exception {
        //given
        User user1 = User.create("email", "password", "노성준", UserSignupType.LOCAL);
        User user2 = User.create("email", "password", "name", UserSignupType.LOCAL);
        User user3 = User.create("email", "password", "name", UserSignupType.LOCAL);
        Space space = Space.create("space", "profileImg");

        User payCreator = userRepository.save(user1);
        User payTarget1 = userRepository.save(user2);
        User payTarget2 = userRepository.save(user3);
        Space testSpace = spaceRepository.save(space);

        UserSpace userSpace = UserSpace.create(payTarget2, testSpace, UserSpaceAuth.NORMAL);
        userSpaceRepository.save(userSpace);

        PayRequest completePay = PayRequest.create(payCreator, testSpace, 20000, "bank", "accountNum");
        completePay.changeCompleteStatus(true);

        PayRequestTarget completeTarget1 = PayRequestTarget.create(completePay, payTarget1.getUserId(), 10000);
        PayRequestTarget completeTarget2 = PayRequestTarget.create(completePay, payTarget2.getUserId(), 10000);
        completeTarget1.changeCompleteStatus(true);
        completeTarget2.changeCompleteStatus(true);

        payRequestRepository.save(completePay);

        payRequestTargetRepository.save(completeTarget1);
        payRequestTargetRepository.save(completeTarget2);

        //when
        PayHomeViewResponse payHomeInfos = payService.getPayHomeInfos(payTarget2.getUserId(), testSpace.getSpaceId());
        List<PayTargetInfoDto> payTargetInfoDtos = payHomeInfos.getPayTargetInfoDtos();

        //then
        assertThat(payTargetInfoDtos).hasSize(0);
    }



}