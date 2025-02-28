
package space.space_spring.domain.pay.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import space.space_spring.domain.discord.application.port.in.CreatePayInDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.CreatePayInDiscordCommand;
import space.space_spring.domain.pay.adapter.in.web.createPay.TargetOfPayRequest;
import space.space_spring.domain.pay.application.port.in.createPay.CreatePayCommand;
import space.space_spring.domain.pay.application.port.out.CreatePayRequestPort;
import space.space_spring.domain.pay.application.port.out.CreatePayRequestTargetPort;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.common.entity.BaseInfo;
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

    private CreatePayRequestPort createPayRequestPort;
    private CreatePayRequestTargetPort createPayRequestTargetPort;
    private LoadSpaceMemberPort loadSpaceMemberPort;
    private CreatePayInDiscordUseCase createPayInDiscordUseCase;
    private CreatePayService createPayService;

    private Long seongjunId;
    private Long sangjunId;
    private Long seohyunId;
    private Long kyungminId;
    private Long jihawnId;          // 지환이는 "알콘" 멤버

    @BeforeEach
    void setUp() {
        createPayRequestPort = Mockito.mock(CreatePayRequestPort.class);
        createPayRequestTargetPort = Mockito.mock(CreatePayRequestTargetPort.class);
        loadSpaceMemberPort = Mockito.mock(LoadSpaceMemberPort.class);
        createPayInDiscordUseCase = Mockito.mock(CreatePayInDiscordUseCase.class);
        createPayService = new CreatePayService(createPayRequestPort, createPayRequestTargetPort, loadSpaceMemberPort, createPayInDiscordUseCase);

        seongjunId = 1L;
        sangjunId = 2L;
        seohyunId = 3L;
        kyungminId = 4L;
        jihawnId = 5L;
    }

    @Test
    @DisplayName("정산 생성자와 정산 대상들이 모두 같은 스페이스에 속해있고, 정산 금액 정책에 위배되지 않을 경우 (= 유효한 command 일 경우) 정산 생성이 처리되고, ID값을 반환한다.")
    void createPay1() throws Exception {
        //given
        TargetOfPayRequest target1 = new TargetOfPayRequest(seongjunId, 10000);
        TargetOfPayRequest target2 = new TargetOfPayRequest(sangjunId, 20000);
        TargetOfPayRequest target3 = new TargetOfPayRequest(seohyunId, 30000);
        List<TargetOfPayRequest> targetOfPayRequests = List.of(target1, target2, target3);

        CreatePayCommand command = createCommand(kyungminId, 60000, targetOfPayRequests, PAY_TYPE_INDIVIDUAL);          // 직접 정산 요청 금액 입력

        // 모든 SpaceMember 들이 같은 스페이스에 속해 있다고 하자
        spaceMembersAreInSameSpace(List.of(kyungminId, seongjunId, sangjunId, seohyunId));

        // createPayInDiscordPort.createPayMessage 메서드로부터 받은 반환 결과는 1L 이라 하자
        Long discordIdForPay = getDiscordIdWillSucceed();

        // 생성한 PayRequest의 id 값은 1L 이라 하자
        PayRequest payRequest = createPayRequest(1L, command, discordIdForPay);
        savePayWillSucceed(payRequest);

        //when
        Long id = createPayService.createPay(command);

        //then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("정산 생성자와 정산 대상들이 모두 같은 스페이스에 속해있고, 정산 금액 정책에 위배되지 않을 경우 (= 유효한 command 일 경우) 정산 생성이 처리되고, ID값을 반환한다.")
    void createPay2() throws Exception {
        //given
        TargetOfPayRequest target1 = new TargetOfPayRequest(seongjunId, 10000);
        TargetOfPayRequest target2 = new TargetOfPayRequest(sangjunId, 10000);
        TargetOfPayRequest target3 = new TargetOfPayRequest(seohyunId, 10000);
        List<TargetOfPayRequest> targetOfPayRequests = List.of(target1, target2, target3);

        CreatePayCommand command = createCommand(kyungminId, 30000, targetOfPayRequests, PAY_TYPE_EQUAL_SPLIT);         // 1/n 정산

        // 모든 SpaceMember 들이 같은 스페이스에 속해 있다고 하자
        spaceMembersAreInSameSpace(List.of(kyungminId, seongjunId, sangjunId, seohyunId));

        // createPayInDiscordPort.createPayMessage 메서드로부터 받은 반환 결과는 1L 이라 하자
        Long discordIdForPay = getDiscordIdWillSucceed();

        // 생성한 PayRequest의 id 값은 1L 이라 하자
        PayRequest payRequest = createPayRequest(1L, command, discordIdForPay);
        savePayWillSucceed(payRequest);

        //when
        Long id = createPayService.createPay(command);

        //then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("정산 생성자와 정산 대상들이 같은 스페이스에 속해있지 않을 경우, 예외를 발생시킨다.")
    void createPay3() throws Exception {
        //given
        TargetOfPayRequest target1 = new TargetOfPayRequest(seongjunId, 10000);
        TargetOfPayRequest target2 = new TargetOfPayRequest(sangjunId, 10000);
        TargetOfPayRequest target3 = new TargetOfPayRequest(jihawnId, 10000);         // target3는 target1, target2 와 다른 스페이스에 있다
        List<TargetOfPayRequest> targetOfPayRequests = List.of(target1, target2, target3);

        CreatePayCommand command = createCommand(seongjunId, 30000, targetOfPayRequests, PAY_TYPE_EQUAL_SPLIT);

        SpaceMember seongjun = Mockito.mock(SpaceMember.class);
        Mockito.when(seongjun.getSpaceId()).thenReturn(1L);
        SpaceMember sangjun = Mockito.mock(SpaceMember.class);
        Mockito.when(sangjun.getSpaceId()).thenReturn(1L);
        SpaceMember jihawn = Mockito.mock(SpaceMember.class);
        Mockito.when(jihawn.getSpaceId()).thenReturn(2L);           // 지환이는 다른 스페이스의 멤버라 하자

        List<SpaceMember> spaceMembers = List.of(seongjun, seongjun, sangjun, jihawn);
        Mockito.when(loadSpaceMemberPort.loadAllById(List.of(seongjunId, seongjunId, sangjunId, jihawnId))).thenReturn(spaceMembers);

        //when //then
        assertThatThrownBy(() -> createPayService.createPay(command))
                .isInstanceOf(CustomException.class)
                .hasMessage(PAY_CREATOR_AND_TARGETS_ARE_NOT_IN_SAME_SPACE.getMessage());
    }

    @Test
    @DisplayName("정산 금액 정책에 위배되는 경우, 예외를 발생시킨다.")
    void createPay4() throws Exception {
        //given
        TargetOfPayRequest target1 = new TargetOfPayRequest(seongjunId, 10000);
        TargetOfPayRequest target2 = new TargetOfPayRequest(sangjunId, 10000);
        TargetOfPayRequest target3 = new TargetOfPayRequest(seohyunId, 15000);
        List<TargetOfPayRequest> targetOfPayRequests = List.of(target1, target2, target3);

        // requested amount 들의 합과 total amount 가 일치하지 않음
        CreatePayCommand command = createCommand(kyungminId, 30000, targetOfPayRequests, PAY_TYPE_INDIVIDUAL);

        spaceMembersAreInSameSpace(List.of(kyungminId, seongjunId, sangjunId, seohyunId));

        //when //then
        assertThatThrownBy(() -> createPayService.createPay(command))
                .isInstanceOf(CustomException.class)
                .hasMessage(INVALID_INDIVIDUAL_AMOUNT.getMessage());
    }

    @Test
    @DisplayName("정산 금액 정책에 위배되는 경우, 예외를 발생시킨다.")
    void createPay5() throws Exception {
        //given
        TargetOfPayRequest target1 = new TargetOfPayRequest(seongjunId, 3333);
        TargetOfPayRequest target2 = new TargetOfPayRequest(sangjunId, 3333);
        TargetOfPayRequest target3 = new TargetOfPayRequest(seohyunId, 3334);
        List<TargetOfPayRequest> targetOfPayRequests = List.of(target1, target2, target3);

        // total amount를 1/n 으로 나눈 몫과 requested amount 가 일치하지 않음
        CreatePayCommand command = createCommand(kyungminId, 10000, targetOfPayRequests, PAY_TYPE_EQUAL_SPLIT);

        // ValidateSpaceMemberUseCase 의 validateSpaceMembersInSameSpace 메서드는 error 를 발생시키지 않는다.
        spaceMembersAreInSameSpace(List.of(kyungminId, seongjunId, sangjunId, seohyunId));

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

    private PayRequest createPayRequest(Long payRequestId, CreatePayCommand command, Long discordIdForPay) {
        BaseInfo baseInfo = BaseInfo.ofEmpty();

        return PayRequest.create(
                payRequestId,
                command.getPayCreatorId(),
                discordIdForPay,
                command.getTotalAmount(),
                command.getTotalTargetNum(),
                command.getBank(),
                command.getPayType(),
                baseInfo
        );
    }

    /**
     * loadSpaceMemberPort.loadAllById() 호출 시,
     * 전달받은 ID 리스트에 해당하는 모든 SpaceMember가 동일한 spaceId(= 1L)를 갖도록 stubbing 한다.
     */
    private void spaceMembersAreInSameSpace(List<Long> spaceMemberIds) {
        Mockito.when(loadSpaceMemberPort.loadAllById(spaceMemberIds)).thenAnswer(invocation -> {
            List<Long> ids = invocation.getArgument(0);
            return ids.stream().map(id -> {
                SpaceMember member = Mockito.mock(SpaceMember.class);
                Mockito.when(member.getSpaceId()).thenReturn(1L);           // SpaceMember 목 객체의 SpaceId 값을 1L로 통일 -> 모든 SpaceMember 들이 같은 Space 에 속한다고 하자
                return member;
            }).toList();
        });
    }

    private Long getDiscordIdWillSucceed() {
        Mockito.when(createPayInDiscordUseCase.createPayInDiscord(any(CreatePayInDiscordCommand.class)))
                .thenReturn(1L);

        return 1L;
    }

    private void savePayWillSucceed(PayRequest payRequest) {
        Mockito.when(createPayRequestPort.createPayRequest(any(PayRequest.class)))
                .thenReturn(payRequest);

        Mockito.when(createPayRequestTargetPort.createPayRequestTargets(anyList()))
                .thenReturn(List.of());         // createPayRequestTargets 메서드의 반환값은 사용하지 X
    }
}
