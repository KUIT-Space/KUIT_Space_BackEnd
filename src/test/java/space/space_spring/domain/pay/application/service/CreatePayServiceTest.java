package space.space_spring.domain.pay.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.pay.adapter.in.web.createPay.TargetOfPayRequest;
import space.space_spring.domain.pay.application.port.in.createPay.CreatePayCommand;
import space.space_spring.domain.pay.application.port.out.CreatePayPort;
import space.space_spring.domain.space.Space;
import space.space_spring.domain.spaceMember.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.SpaceMember;
import space.space_spring.domain.user.User;
import space.space_spring.global.exception.CustomException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

class CreatePayServiceTest {

    private final String PAY_TYPE_INDIVIDUAL = "INDIVIDUAL";
    private final String PAY_TYPE_EQUAL_SPLIT = "EQUAL_SPLIT";

    private final CreatePayPort createPayPort = Mockito.mock(CreatePayPort.class);
    private final LoadSpaceMemberPort loadSpaceMemberPort = Mockito.mock(LoadSpaceMemberPort.class);
    private CreatePayService createPayService;

    private Space kuit;
    private Space alcon;

    private SpaceMember seongjun;
    private SpaceMember sangjun;
    private SpaceMember seohyun;
    private SpaceMember kyeongmin;
    private SpaceMember jihwan;

    @BeforeEach
    void setUp() {
        createPayService = new CreatePayService(createPayPort, loadSpaceMemberPort);
        User commonUser = User.create(1L, 1L);          // User 도메인 엔티티는 그냥 하나로 공유해서 테스트 진행
        kuit = Space.create(1L, "쿠잇", 1L);
        alcon = Space.create(2L, "알콘", 2L);

        seongjun = SpaceMember.create(1L, kuit, commonUser, 1L, "노성준", "image_111", true);
        sangjun = SpaceMember.create(2L, kuit, commonUser, 2L, "개구리비안", "image_222", false);
        seohyun = SpaceMember.create(3L, kuit, commonUser, 3L, "정서현", "image_333", false);
        kyeongmin = SpaceMember.create(4L, kuit, commonUser, 4L, "김경민", "image_444", false);
        jihwan = SpaceMember.create(5L, alcon, commonUser, 5L, "김지환", "image_555", false);

        // Mockito Stubbing : 특정 ID가 들어오면 그에 맞는 SpaceMember 반환
        Mockito.when(loadSpaceMemberPort.loadSpaceMember(seongjun.getId()))
                .thenReturn(seongjun);
        Mockito.when(loadSpaceMemberPort.loadSpaceMember(sangjun.getId()))
                .thenReturn(sangjun);
        Mockito.when(loadSpaceMemberPort.loadSpaceMember(seohyun.getId()))
                .thenReturn(seohyun);
        Mockito.when(loadSpaceMemberPort.loadSpaceMember(kyeongmin.getId()))
                .thenReturn(kyeongmin);
        Mockito.when(loadSpaceMemberPort.loadSpaceMember(jihwan.getId()))
                .thenReturn(jihwan);
    }

    @Test
    @DisplayName("정산 생성자와 정산 대상들이 모두 같은 스페이스에 속해있고, 정산 금액 정책에 위배되지 않을 경우 (= 유효한 command 일 경우) 정산 생성이 처리되고, ID값을 반환한다.")
    void createPay1() throws Exception {
        //given
        TargetOfPayRequest target1 = new TargetOfPayRequest(sangjun.getId(), 10000);
        TargetOfPayRequest target2 = new TargetOfPayRequest(seohyun.getId(), 20000);
        TargetOfPayRequest target3 = new TargetOfPayRequest(kyeongmin.getId(), 30000);
        List<TargetOfPayRequest> targetOfPayRequests = List.of(target1, target2, target3);

        CreatePayCommand command = createCommand(seongjun, 60000, targetOfPayRequests, PAY_TYPE_INDIVIDUAL);
        savePayWillSucceed();

        //when
        Long id = createPayService.createPay(command);

        //then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("정산 생성자와 정산 대상들이 모두 같은 스페이스에 속해있고, 정산 금액 정책에 위배되지 않을 경우 (= 유효한 command 일 경우) 정산 생성이 처리되고, ID값을 반환한다.")
    void createPay2() throws Exception {
        //given
        TargetOfPayRequest target1 = new TargetOfPayRequest(sangjun.getId(), 10000);
        TargetOfPayRequest target2 = new TargetOfPayRequest(seohyun.getId(), 10000);
        TargetOfPayRequest target3 = new TargetOfPayRequest(kyeongmin.getId(), 10000);
        List<TargetOfPayRequest> targetOfPayRequests = List.of(target1, target2, target3);

        CreatePayCommand command = createCommand(seongjun, 30000, targetOfPayRequests, PAY_TYPE_EQUAL_SPLIT);
        savePayWillSucceed();

        //when
        Long id = createPayService.createPay(command);

        //then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("정산 생성자와 정산 대상들이 같은 스페이스에 속해있지 않을 경우, 예외를 발생시킨다.")
    void createPay3() throws Exception {
        //given
        TargetOfPayRequest target1 = new TargetOfPayRequest(sangjun.getId(), 10000);
        TargetOfPayRequest target2 = new TargetOfPayRequest(seohyun.getId(), 10000);
        TargetOfPayRequest target3 = new TargetOfPayRequest(jihwan.getId(), 10000);         // alcon 동아리의 지환이에게 정산 요청
        List<TargetOfPayRequest> targetOfPayRequests = List.of(target1, target2, target3);

        CreatePayCommand command = createCommand(seongjun, 30000, targetOfPayRequests, PAY_TYPE_EQUAL_SPLIT);
        savePayWillSucceed();

        //when //then
        assertThatThrownBy(() -> createPayService.createPay(command))
                .isInstanceOf(CustomException.class)
                .hasMessage(PAY_CREATOR_AND_TARGETS_ARE_NOT_IN_SAME_SPACE.getMessage());
    }

    @Test
    @DisplayName("정산 금액 정책에 위배되는 경우, 예외를 발생시킨다.")
    void createPay4() throws Exception {
        //given
        TargetOfPayRequest target1 = new TargetOfPayRequest(sangjun.getId(), 10000);
        TargetOfPayRequest target2 = new TargetOfPayRequest(seohyun.getId(), 10000);
        TargetOfPayRequest target3 = new TargetOfPayRequest(kyeongmin.getId(), 15000);
        List<TargetOfPayRequest> targetOfPayRequests = List.of(target1, target2, target3);

        // requested amount 들의 합과 total amount 가 일치하지 않음
        CreatePayCommand command = createCommand(seongjun, 30000, targetOfPayRequests, PAY_TYPE_INDIVIDUAL);
        savePayWillSucceed();

        //when //then
        assertThatThrownBy(() -> createPayService.createPay(command))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_INDIVIDUAL_AMOUNT.getMessage());
    }

    @Test
    @DisplayName("정산 금액 정책에 위배되는 경우, 예외를 발생시킨다.")
    void createPay5() throws Exception {
        //given
        TargetOfPayRequest target1 = new TargetOfPayRequest(sangjun.getId(), 3333);
        TargetOfPayRequest target2 = new TargetOfPayRequest(seohyun.getId(), 3333);
        TargetOfPayRequest target3 = new TargetOfPayRequest(kyeongmin.getId(), 3334);
        List<TargetOfPayRequest> targetOfPayRequests = List.of(target1, target2, target3);

        // total amount를 1/n 으로 나눈 몫과 requested amount 가 일치하지 않음
        CreatePayCommand command = createCommand(seongjun, 10000, targetOfPayRequests, PAY_TYPE_EQUAL_SPLIT);
        savePayWillSucceed();

        //when //then
        assertThatThrownBy(() -> createPayService.createPay(command))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_EQUAL_SPLIT_AMOUNT.getMessage());
    }

    private CreatePayCommand createCommand(SpaceMember payCreator, int totalAmount, List<TargetOfPayRequest> targetOfPayRequests, String payType) {
        return CreatePayCommand.create(
                payCreator.getId(),
                totalAmount,
                "우리은행",
                "111-111-111",
                targetOfPayRequests,
                payType
        );
    }

    private void savePayWillSucceed() {
        Mockito.when(createPayPort.savePay(Mockito.any(), Mockito.anyList()))
                .thenReturn(1L);
    }
}