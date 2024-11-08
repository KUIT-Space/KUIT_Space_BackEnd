package space.space_spring.domain.pay.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
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
    @DisplayName("유저가 요청한 정산 중, 현재 진행 중인 정산 정보들을 return 한다.")
    void getPayHomeInfos() throws Exception {
        //given
        User user1 = User.create("email", "password", "name", UserSignupType.LOCAL);
        User user2 = User.create("email", "password", "name", UserSignupType.LOCAL);
        User user3 = User.create("email", "password", "name", UserSignupType.LOCAL);
        Space space = Space.create("space", "profileImg");

        User testUser1 = userRepository.save(user1);
        User testUser2 = userRepository.save(user2);
        User testUser3 = userRepository.save(user3);
        Space testSpace = spaceRepository.save(space);

        UserSpace userSpace = UserSpace.create(testUser1, testSpace, UserSpaceAuth.NORMAL);
        userSpaceRepository.save(userSpace);

        PayRequest inCompletePay = PayRequest.create(testUser1, testSpace, 20000, "bank", "accountNum", 20000, false);
        PayRequest completePay = PayRequest.create(testUser1, testSpace, 20000, "bank", "accountNum", 20000, true);

        PayRequestTarget inCompleteTarget1 = PayRequestTarget.create(inCompletePay, testUser2.getUserId(), 10000, false);
        PayRequestTarget inCompleteTarget2 = PayRequestTarget.create(inCompletePay, testUser3.getUserId(), 10000, false);
        PayRequestTarget completeTarget1 = PayRequestTarget.create(completePay, testUser2.getUserId(), 10000, true);
        PayRequestTarget completeTarget2 = PayRequestTarget.create(completePay, testUser3.getUserId(), 10000, true);

        payRequestRepository.save(inCompletePay);
        payRequestRepository.save(completePay);

        payRequestTargetRepository.save(inCompleteTarget1);
        payRequestTargetRepository.save(inCompleteTarget2);
        payRequestTargetRepository.save(completeTarget1);
        payRequestTargetRepository.save(completeTarget2);

        //when
        PayHomeViewResponse payHomeInfos = payService.getPayHomeInfos(testUser1.getUserId(), testSpace.getSpaceId());
        List<PayRequestInfoDto> payRequestInfos = payHomeInfos.getPayRequestInfoDtoList();

        //then
        assertThat(payRequestInfos).hasSize(1)
                .extracting("totalAmount", "receiveAmount", "totalTargetNum", "receiveTargetNum")
                .containsExactlyInAnyOrder(
                        tuple(20000, 0, 2, 0)
                );
    }

}