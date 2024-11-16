package space.space_spring.domain.pay.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
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
import space.space_spring.exception.CustomException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static space.space_spring.response.status.BaseExceptionResponseStatus.USER_IS_NOT_IN_SPACE;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
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

    private User seongjun;
    private User sangjun;
    private User seohyun;
    private User kyeongmin;
    private Space kuit;

    @BeforeEach
    void setUp() {
        // kuit 에 성준, 상준, 서현, 경민 이 가입되어 있음
        seongjun = userRepository.save(User.create("email1", "password", "노성준", UserSignupType.LOCAL));
        sangjun = userRepository.save(User.create("email2", "password", "김상준", UserSignupType.LOCAL));
        seohyun = userRepository.save(User.create("email3", "password", "정서현", UserSignupType.LOCAL));
        kyeongmin = userRepository.save(User.create("email4", "password", "김경민", UserSignupType.LOCAL));

        kuit = spaceRepository.save(Space.create("space", "profileImg"));

        userSpaceRepository.save(UserSpace.create(seongjun, kuit, UserSpaceAuth.NORMAL));
        userSpaceRepository.save(UserSpace.create(sangjun, kuit, UserSpaceAuth.NORMAL));
        userSpaceRepository.save(UserSpace.create(seohyun, kuit, UserSpaceAuth.NORMAL));
        userSpaceRepository.save(UserSpace.create(kyeongmin, kuit, UserSpaceAuth.NORMAL));
    }

    @Test
    @DisplayName("유저가 요청한 정산들 중 완료되지 않은 정산 정보들과, 요청받은 정산들 중 완료되지 않은 정산 정보들을 return 한다.")
    void getPayHomeInfos1() throws Exception {
        //given
        // seongjun 이 kuit 에서 생성한 3개의 정산 요청
        PayRequest inCompletePay1_seongjun = PayRequest.create(seongjun, kuit, 20000, "bank", "accountNum");
        PayRequest inCompletePay2_seongjun = PayRequest.create(seongjun, kuit, 40000, "bank", "accountNum");
        PayRequest completePay_seongjun = PayRequest.create(seongjun, kuit, 20000, "bank", "accountNum");
        completePay_seongjun.changeCompleteStatus(true);

        // seohyun 이 kuit 에서 생성한 1개의 정산 요청
        PayRequest inCompletePay_seohyun = PayRequest.create(seohyun, kuit, 40000, "국민은행", "111-111");

        // Kyeongmin 이 kuit 에서 생성한 1개의 정산 요청
        PayRequest completePay_kyeongmin = PayRequest.create(kyeongmin, kuit, 20000, "우리은행", "222-222");
        completePay_kyeongmin.changeCompleteStatus(true);

        // 각 정산 요청의 타겟들
        PayRequestTarget inCompleteTarget1 = PayRequestTarget.create(inCompletePay1_seongjun, sangjun.getUserId(), 10000);
        PayRequestTarget inCompleteTarget2 = PayRequestTarget.create(inCompletePay1_seongjun, kyeongmin.getUserId(), 10000);

        PayRequestTarget inCompleteTarget3 = PayRequestTarget.create(inCompletePay2_seongjun, seohyun.getUserId(), 20000);
        PayRequestTarget inCompleteTarget4 = PayRequestTarget.create(inCompletePay2_seongjun, kyeongmin.getUserId(), 20000);

        PayRequestTarget completeTarget1 = PayRequestTarget.create(completePay_seongjun, sangjun.getUserId(), 10000);
        PayRequestTarget completeTarget2 = PayRequestTarget.create(completePay_seongjun, seohyun.getUserId(), 10000);
        completeTarget1.changeCompleteStatus(true);
        completeTarget2.changeCompleteStatus(true);

        PayRequestTarget inCompleteTarget5 = PayRequestTarget.create(inCompletePay_seohyun, seongjun.getUserId(), 20000);
        PayRequestTarget inCompleteTarget6 = PayRequestTarget.create(inCompletePay_seohyun, sangjun.getUserId(), 20000);

        PayRequestTarget completeTarget3 = PayRequestTarget.create(completePay_kyeongmin, seongjun.getUserId(), 20000);
        completeTarget3.changeCompleteStatus(true);

        payRequestTargetRepository.save(inCompleteTarget1);
        payRequestTargetRepository.save(inCompleteTarget2);
        payRequestTargetRepository.save(inCompleteTarget3);
        payRequestTargetRepository.save(inCompleteTarget4);
        payRequestTargetRepository.save(completeTarget1);
        payRequestTargetRepository.save(completeTarget2);
        payRequestTargetRepository.save(inCompleteTarget5);
        payRequestTargetRepository.save(inCompleteTarget6);
        payRequestTargetRepository.save(completeTarget3);

        payRequestRepository.save(inCompletePay1_seongjun);
        payRequestRepository.save(inCompletePay2_seongjun);
        payRequestRepository.save(completePay_seongjun);
        payRequestRepository.save(inCompletePay_seohyun);
        payRequestRepository.save(completePay_kyeongmin);

        //when
        PayHomeViewResponse payHomeInfos = payService.getPayHomeInfos(seongjun.getUserId(), kuit.getSpaceId());
        List<PayRequestInfoDto> payRequestInfos = payHomeInfos.getPayRequestInfoDtos();
        List<PayTargetInfoDto> payTargetInfoDtos = payHomeInfos.getPayTargetInfoDtos();

        //then
        assertThat(payRequestInfos).hasSize(2)
                .extracting("payRequestId", "totalAmount", "receiveAmount", "totalTargetNum", "paySendTargetNum")
                .containsExactlyInAnyOrder(
                        tuple(inCompletePay1_seongjun.getPayRequestId(), 20000, 0, 2, 0),
                        tuple(inCompletePay2_seongjun.getPayRequestId(), 40000, 0, 2, 0)
                );

        assertThat(payTargetInfoDtos).hasSize(1)
                .extracting("payRequestTargetId", "payCreatorName", "requestedAmount", "bankName", "bankAccountNum")
                .containsExactlyInAnyOrder(
                        tuple(inCompleteTarget5.getPayRequestTargetId(), "정서현", 20000, "국민은행", "111-111")
                );
    }


    @Test
    @DisplayName("유저가 요청한 정산들 중, 완료되지 않은 정산이 없으면 빈 ArrayList 를 return 한다.")
    void getPayHomeInfos2() throws Exception {
        //given
        PayRequest completePay = PayRequest.create(seongjun, kuit, 20000, "bank", "accountNum");
        completePay.changeCompleteStatus(true);

        PayRequestTarget completeTarget1 = PayRequestTarget.create(completePay, sangjun.getUserId(), 10000);
        PayRequestTarget completeTarget2 = PayRequestTarget.create(completePay, seohyun.getUserId(), 10000);
        completeTarget1.changeCompleteStatus(true);
        completeTarget2.changeCompleteStatus(true);

        payRequestRepository.save(completePay);

        payRequestTargetRepository.save(completeTarget1);
        payRequestTargetRepository.save(completeTarget2);

        //when
        PayHomeViewResponse payHomeInfos = payService.getPayHomeInfos(seongjun.getUserId(), kuit.getSpaceId());
        List<PayRequestInfoDto> payRequestInfoDtoList = payHomeInfos.getPayRequestInfoDtos();

        //then
        assertThat(payRequestInfoDtoList).isNotNull()
                .hasSize(0);
    }

    @Test
    @DisplayName("유저가 요청받은 정산들 중, 완료되지 않은 정산이 없으면 빈 ArrayList를 return 한다.")
    void getPayHomeInfos3() throws Exception {
        //given
        PayRequest completePay = PayRequest.create(seongjun, kuit, 20000, "bank", "accountNum");
        completePay.changeCompleteStatus(true);

        PayRequestTarget completeTarget1 = PayRequestTarget.create(completePay, seohyun.getUserId(), 10000);
        PayRequestTarget completeTarget2 = PayRequestTarget.create(completePay, kyeongmin.getUserId(), 10000);
        completeTarget1.changeCompleteStatus(true);
        completeTarget2.changeCompleteStatus(true);

        payRequestRepository.save(completePay);

        payRequestTargetRepository.save(completeTarget1);
        payRequestTargetRepository.save(completeTarget2);

        //when
        PayHomeViewResponse payHomeInfos = payService.getPayHomeInfos(kyeongmin.getUserId(), kuit.getSpaceId());
        List<PayTargetInfoDto> payTargetInfoDtos = payHomeInfos.getPayTargetInfoDtos();

        //then
        assertThat(payTargetInfoDtos).isNotNull()
                .hasSize(0);
    }

    @Test
    @DisplayName("user는 자신이 속한 space의 정산 홈 view만을 볼 수 있다.")
    void getPayHomeInfos4() throws Exception {
        //given
        Space alcon = spaceRepository.save(Space.create("space", "profileImg"));

        PayRequest payRequest = PayRequest.create(seongjun, kuit, 20000, "bank", "accountNum");

        PayRequestTarget target1 = PayRequestTarget.create(payRequest, sangjun.getUserId(), 10000);
        PayRequestTarget target2 = PayRequestTarget.create(payRequest, seohyun.getUserId(), 10000);

        payRequestRepository.save(payRequest);

        payRequestTargetRepository.save(target1);
        payRequestTargetRepository.save(target2);

        //when // then
        assertThatThrownBy(() -> payService.getPayHomeInfos(seongjun.getUserId(), alcon.getSpaceId()))
                .isInstanceOf(CustomException.class)
                .hasMessage(USER_IS_NOT_IN_SPACE.getMessage());
    }




}