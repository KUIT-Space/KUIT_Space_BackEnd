package space.space_spring.domain.pay.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.discord.application.port.in.CreatePayInDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.CreatePayInDiscordCommand;
import space.space_spring.domain.pay.adapter.in.web.createPay.TargetOfPayRequest;
import space.space_spring.domain.pay.application.port.in.createPay.CreatePayCommand;
import space.space_spring.domain.pay.application.port.out.CreatePayPort;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.spaceMember.ValidateSpaceMemberUseCase;
import space.space_spring.global.exception.CustomException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

class CreatePayServiceTest {

    private final String PAY_TYPE_INDIVIDUAL = "INDIVIDUAL";
    private final String PAY_TYPE_EQUAL_SPLIT = "EQUAL_SPLIT";

    private final CreatePayPort createPayPort = Mockito.mock(CreatePayPort.class);
    private final ValidateSpaceMemberUseCase validateSpaceMemberUseCase = Mockito.mock(ValidateSpaceMemberUseCase.class);
    private final CreatePayInDiscordUseCase createPayInDiscordUseCase = Mockito.mock(CreatePayInDiscordUseCase.class);
    private CreatePayService createPayService;

    @BeforeEach
    void setUp() {
        createPayService = new CreatePayService(createPayPort, validateSpaceMemberUseCase, createPayInDiscordUseCase);
    }

    @Test
    @DisplayName("정산 생성자와 정산 대상들이 모두 같은 스페이스에 속해있고, 정산 금액 정책에 위배되지 않을 경우 (= 유효한 command 일 경우) 정산 생성이 처리되고, ID값을 반환한다.")
    void createPay1() throws Exception {
        //given
        TargetOfPayRequest target1 = new TargetOfPayRequest(1L, 10000);
        TargetOfPayRequest target2 = new TargetOfPayRequest(2L, 20000);
        TargetOfPayRequest target3 = new TargetOfPayRequest(3L, 30000);
        List<TargetOfPayRequest> targetOfPayRequests = List.of(target1, target2, target3);

        CreatePayCommand command = createCommand(1L, 60000, targetOfPayRequests, PAY_TYPE_INDIVIDUAL);

        // ValidateSpaceMemberUseCase 의 validateSpaceMembersInSameSpace 메서드는 error 를 발생시키지 않는다.
        spaceMembersAreInSameSpace();

        // createPayInDiscordPort.createPayMessage 메서드로부터 받은 반환 결과는 1L
        Long discordIdForPay = getDiscordIdWillSucceed();

        // CreatePayPort 의 createPayRequest 메서드로부터 받은 반환 결과는 1L
        PayRequest payRequest = createPayRequest(1L, command, discordIdForPay);

        savePayWillSucceed(payRequest);

        //when
        Long id = createPayService.createPay(command);

        //then
        assertThat(id).isEqualTo(1L);
    }

    private PayRequest createPayRequest(Long payRequestId, CreatePayCommand command, Long discordIdForPay) {
        return PayRequest.of(
                payRequestId,
                command.getPayCreatorId(),
                discordIdForPay,
                command.getTotalAmount(),
                command.getTotalTargetNum(),
                command.getBank(),
                command.getPayType()
        );
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

    private CreatePayCommand createCommand(Long payCreatorId, int totalAmount, List<TargetOfPayRequest> targetOfPayRequests, String payType) {
        return CreatePayCommand.builder()
                .payCreatorId(payCreatorId)
                .totalAmount(totalAmount)
                .bankName("우리 은행")
                .bankAccountNum("111-111")
                .targets(targetOfPayRequests)
                .valueOfPayType(payType)
                .build();
    }

    private void spaceMembersAreInSameSpace() {
        Mockito.doNothing().when(validateSpaceMemberUseCase).validateSpaceMembersInSameSpace(anyList());
    }

    private Long getDiscordIdWillSucceed() {
        Mockito.when(createPayInDiscordUseCase.createPayInDiscord(any(CreatePayInDiscordCommand.class)))
                .thenReturn(1L);

        return 1L;
    }

    private void savePayWillSucceed(PayRequest payRequest) {
        Mockito.when(createPayPort.createPayRequest(any(PayRequest.class)))
                .thenReturn(payRequest);

        Mockito.when(createPayPort.createPayRequestTargets(anyList()))
                .thenReturn(List.of());         // createPayRequestTargets 메서드의 반환값은 사용하지 X
    }
}