package space.space_spring.domain.pay.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.pay.model.PayCreateTargetInfo;
import space.space_spring.domain.pay.model.PayType;
import space.space_spring.domain.pay.model.dto.PayTargetInfoDto;
import space.space_spring.domain.pay.model.request.PayCreateServiceRequest;
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
import space.space_spring.global.common.enumStatus.UserSignupType;
import space.space_spring.global.common.enumStatus.UserSpaceAuth;
import space.space_spring.global.exception.CustomException;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

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
        PayRequest inCompletePay1_seongjun = PayRequest.create(seongjun, kuit, 20000, "bank", "accountNum", PayType.INDIVIDUAL);
        PayRequest inCompletePay2_seongjun = PayRequest.create(seongjun, kuit, 40000, "bank", "accountNum", PayType.INDIVIDUAL);
        PayRequest completePay_seongjun = PayRequest.create(seongjun, kuit, 20000, "bank", "accountNum", PayType.INDIVIDUAL);
        completePay_seongjun.changeCompleteStatus(true);

        // seohyun 이 kuit 에서 생성한 1개의 정산 요청
        PayRequest inCompletePay_seohyun = PayRequest.create(seohyun, kuit, 40000, "국민은행", "111-111", PayType.INDIVIDUAL);

        // Kyeongmin 이 kuit 에서 생성한 1개의 정산 요청
        PayRequest completePay_kyeongmin = PayRequest.create(kyeongmin, kuit, 20000, "우리은행", "222-222", PayType.INDIVIDUAL);
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
        PayRequest completePay = PayRequest.create(seongjun, kuit, 20000, "bank", "accountNum", PayType.INDIVIDUAL);
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
    @DisplayName("유저가 요청받은 정산들 중, 완료되지 않은 정산이 없으면 빈 ArrayList 를 return 한다.")
    void getPayHomeInfos3() throws Exception {
        //given
        PayRequest completePay = PayRequest.create(seongjun, kuit, 20000, "bank", "accountNum", PayType.INDIVIDUAL);
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
    @DisplayName("유저는 자신이 속한 스페이스의 정산 홈 view 만을 볼 수 있다.")
    void getPayHomeInfos4() throws Exception {
        //given
        Space alcon = spaceRepository.save(Space.create("space", "profileImg"));

        PayRequest payRequest = PayRequest.create(seongjun, kuit, 20000, "bank", "accountNum", PayType.INDIVIDUAL);

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

    @Test
    @DisplayName("유저는 자신과 동일한 스페이스에 속한 유저들에게 [금액 직접 입력] 정책으로 정산을 생성할 수 있다.")
    void createPay1() throws Exception {
        //given
        PayCreateTargetInfo targetIsSeongjun = createPayCreateTargetInfo(seongjun, 12000);
        PayCreateTargetInfo targetIsSangjun = createPayCreateTargetInfo(sangjun, 15000);
        PayCreateTargetInfo targetIsSeohyun = createPayCreateTargetInfo(seohyun, 8000);
        PayCreateTargetInfo targetIsKyeongmin = createPayCreateTargetInfo(kyeongmin, 5000);
        List<PayCreateTargetInfo> targetInfos = List.of(targetIsSeongjun, targetIsSangjun, targetIsSeohyun, targetIsKyeongmin);

        PayCreateServiceRequest serviceRequest = createServiceRequest(40000, targetInfos, PayType.INDIVIDUAL);

        //when
        Long savedPayRequestId = payService.createPay(seongjun.getUserId(), kuit.getSpaceId(), serviceRequest);

        //then
        PayRequest savedPayRequest = payRequestRepository.findById(savedPayRequestId)
                .orElseThrow(() -> new AssertionError("PayRequest 가 제대로 생성되지 않았습니다."));

        assertThat(savedPayRequest).extracting("payCreateUser", "space", "totalAmount", "bankName", "bankAccountNum", "payType")
                .contains(seongjun, kuit, 40000, "우리은행", "111-111", PayType.INDIVIDUAL);

        List<PayRequestTarget> allByPayRequest = payRequestTargetRepository.findAllByPayRequest(savedPayRequest);

        assertThat(allByPayRequest).hasSize(4)
                .extracting("targetUserId", "requestedAmount")
                .containsExactlyInAnyOrder(
                        tuple(seongjun.getUserId(), 12000),
                        tuple(sangjun.getUserId(), 15000),
                        tuple(seohyun.getUserId(), 8000),
                        tuple(kyeongmin.getUserId(), 5000)
                );
    }

    @Test
    @DisplayName("유저는 자신과 동일한 스페이스에 속한 유저들에게 [1/N 정산하기] 정책으로 정산을 생성할 수 있다.")
    void createPay2() throws Exception {
        PayCreateTargetInfo targetIsSeongjun = createPayCreateTargetInfo(seongjun, 3333);
        PayCreateTargetInfo targetIsSangjun = createPayCreateTargetInfo(sangjun, 3333);
        PayCreateTargetInfo targetIsSeohyun = createPayCreateTargetInfo(seohyun, 3333);
        List<PayCreateTargetInfo> targetInfos = List.of(targetIsSeongjun, targetIsSangjun, targetIsSeohyun);

        PayCreateServiceRequest serviceRequest = createServiceRequest(10000, targetInfos, PayType.EQUAL_SPLIT);

        //when
        Long savedPayRequestId = payService.createPay(seongjun.getUserId(), kuit.getSpaceId(), serviceRequest);

        //then
        PayRequest savedPayRequest = payRequestRepository.findById(savedPayRequestId)
                .orElseThrow(() -> new AssertionError("PayRequest 가 제대로 생성되지 않았습니다."));

        assertThat(savedPayRequest).extracting("payCreateUser", "space", "totalAmount", "bankName", "bankAccountNum", "payType")
                .contains(seongjun, kuit, 10000, "우리은행", "111-111", PayType.EQUAL_SPLIT);

        List<PayRequestTarget> allByPayRequest = payRequestTargetRepository.findAllByPayRequest(savedPayRequest);

        assertThat(allByPayRequest).hasSize(3)
                .extracting("targetUserId", "requestedAmount")
                .containsExactlyInAnyOrder(
                        tuple(seongjun.getUserId(), 3333),
                        tuple(sangjun.getUserId(), 3333),
                        tuple(seohyun.getUserId(), 3333)
                );
    }

    @Test
    @DisplayName("유저는 본인이 속한 스페이스에서만 정산을 생성할 수 있다.")
    void createPay3() throws Exception {
        //given
        PayCreateTargetInfo targetIsSeohyun = createPayCreateTargetInfo(seohyun, 8000);
        PayCreateTargetInfo targetIsKyeongmin = createPayCreateTargetInfo(kyeongmin, 5000);
        List<PayCreateTargetInfo> targetInfos = List.of(targetIsSeohyun, targetIsKyeongmin);

        // alcon 동아리에 속해있는 양석준씨
        User seokjun = userRepository.save(User.create("email1", "password", "양석준", UserSignupType.LOCAL));
        Space alcon = spaceRepository.save(Space.create("space", "profileImg"));
        userSpaceRepository.save(UserSpace.create(seokjun, alcon, UserSpaceAuth.NORMAL));

        PayCreateServiceRequest serviceRequest = createServiceRequest(13000, targetInfos, PayType.INDIVIDUAL);

        //when //then
        assertThatThrownBy(() -> payService.createPay(seokjun.getUserId(), kuit.getSpaceId(), serviceRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(PAY_CREATOR_IS_NOT_IN_SPACE.getMessage());
    }

    @Test
    @DisplayName("자신과 다른 스페이스에 속한 유저에게는 정산을 생성할 수 없다.")
    void createPay4() throws Exception {
        //given
        // alcon 동아리에 속해있는 양석준씨
        User seokjun = userRepository.save(User.create("email1", "password", "양석준", UserSignupType.LOCAL));
        Space alcon = spaceRepository.save(Space.create("space", "profileImg"));
        userSpaceRepository.save(UserSpace.create(seokjun, alcon, UserSpaceAuth.NORMAL));

        PayCreateTargetInfo targetIsSeokjun = createPayCreateTargetInfo(seokjun, 12000);
        PayCreateTargetInfo targetIsKyeongmin = createPayCreateTargetInfo(kyeongmin, 5000);
        List<PayCreateTargetInfo> targetInfos = List.of(targetIsSeokjun, targetIsKyeongmin);

        PayCreateServiceRequest serviceRequest = createServiceRequest(17000, targetInfos, PayType.INDIVIDUAL);

        //when //then
        assertThatThrownBy(() -> payService.createPay(seongjun.getUserId(), kuit.getSpaceId(), serviceRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(PAY_TARGET_IS_NOT_IN_SPACE.getMessage());
    }

    @Test
    @DisplayName("[금액 직접 입력] 정책을 따르는 정산에서 정산 타겟들에게 요청한 금액들의 합이 totalAmount 와 다를 경우, 예외가 발생한다.")
    void createPay5() throws Exception {
        //given
        // 타겟들에게 요청한 금액들의 합은 40000원
        PayCreateTargetInfo targetIsSeongjun = createPayCreateTargetInfo(seongjun, 12000);
        PayCreateTargetInfo targetIsSangjun = createPayCreateTargetInfo(sangjun, 15000);
        PayCreateTargetInfo targetIsSeohyun = createPayCreateTargetInfo(seohyun, 8000);
        PayCreateTargetInfo targetIsKyeongmin = createPayCreateTargetInfo(kyeongmin, 5000);
        List<PayCreateTargetInfo> targetInfos = List.of(targetIsSeongjun, targetIsSangjun, targetIsSeohyun, targetIsKyeongmin);

        PayCreateServiceRequest serviceRequest = createServiceRequest(30000, targetInfos, PayType.INDIVIDUAL);

        //when //then
        assertThatThrownBy(() -> payService.createPay(seongjun.getUserId(), kuit.getSpaceId(), serviceRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_INDIVIDUAL_AMOUNT.getMessage());
    }

    @Test
    @DisplayName("[1/N 정산하기] 정책을 따르는 정산에서 정산 타겟들에게 요청한 금액이 totalAmount/N 과 다를 경우, 예외가 발생한다.")
    void createPay6() throws Exception {
        //given
        // 타겟들에게 요청한 금액들은 3333원 이어야 함
        PayCreateTargetInfo targetIsSeongjun = createPayCreateTargetInfo(seongjun, 3333);
        PayCreateTargetInfo targetIsSangjun = createPayCreateTargetInfo(sangjun, 3333);
        PayCreateTargetInfo targetIsSeohyun = createPayCreateTargetInfo(seohyun, 3334);     // != 3333
        List<PayCreateTargetInfo> targetInfos = List.of(targetIsSeongjun, targetIsSangjun, targetIsSeohyun);

        PayCreateServiceRequest serviceRequest = createServiceRequest(10000, targetInfos, PayType.EQUAL_SPLIT);

        //when //then
        assertThatThrownBy(() -> payService.createPay(seongjun.getUserId(), kuit.getSpaceId(), serviceRequest))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_EQUAL_SPLIT_AMOUNT.getMessage());
    }

    private PayCreateTargetInfo createPayCreateTargetInfo(User user, int requestedAmount) {
        return PayCreateTargetInfo.builder()
                .targetUserId(user.getUserId())
                .requestedAmount(requestedAmount)
                .build();
    }

    private PayCreateServiceRequest createServiceRequest(int totalAmount, List<PayCreateTargetInfo> targetInfos, PayType payType) {
        return PayCreateServiceRequest.builder()
                .totalAmount(totalAmount)
                .bankName("우리은행")
                .bankAccountNum("111-111")
                .payCreateTargetInfos(targetInfos)
                .payType(payType)
                .build();
    }
}