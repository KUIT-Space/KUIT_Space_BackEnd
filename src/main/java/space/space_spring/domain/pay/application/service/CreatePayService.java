package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.discord.application.port.in.CreatePayInDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.CreatePayInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.TargetOfCreatePayInDiscordCommand;
import space.space_spring.domain.pay.application.port.in.createPay.CreatePayCommand;
import space.space_spring.domain.pay.application.port.in.createPay.CreatePayUseCase;
import space.space_spring.domain.pay.application.port.in.createPay.TargetOfCreatePayCommand;
import space.space_spring.domain.pay.application.port.out.CreatePayPort;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayAmountPolicy;
import space.space_spring.domain.spaceMember.ValidateSpaceMemberUseCase;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatePayService implements CreatePayUseCase {

    private final CreatePayPort createPayPort;
    private final ValidateSpaceMemberUseCase validateSpaceMemberUseCase;
    private final CreatePayInDiscordUseCase createPayInDiscordUseCase;

    @Override
    @Transactional
    public Long createPay(CreatePayCommand command) {
        // 정산 생성자, 정산 대상자들이 같은 스페이스에 존재하는지 검증
        validateSpaceMemberUseCase.validateSpaceMembersInSameSpace(getSpaceMemberIds(command));

        // PayAmountPolicy 유효성 검증
        validatePayAmountPolicy(command);

        // 디스코드로 보내기
        Long discordIdForPay = createPayInDiscordUseCase.createPayInDiscord(mapToDiscordCommand(command));

        // PayRequest 도메인 엔티티 생성 & 저장
        PayRequest savedPayRequest = savePayRequest(command, discordIdForPay);

        // PayRequestTarget 도메인 엔티티들 생성 & 저장
        savePayRequestTargets(command, savedPayRequest);

        return savedPayRequest.getId();
    }

    private List<Long> getSpaceMemberIds(CreatePayCommand command) {
        List<Long> spaceMemberIds = new ArrayList<>();
        spaceMemberIds.add(command.getPayCreatorId());
        for (TargetOfCreatePayCommand target : command.getTargets()) {
            spaceMemberIds.add(target.getTargetMemberId());
        }
        return spaceMemberIds;
    }

    private void validatePayAmountPolicy(CreatePayCommand command) {
        PayAmountPolicy payAmountPolicy = command.getPayType().getPayAmountPolicy();

        List<Money> targetAmounts = command.getTargets().stream()
                .map(TargetOfCreatePayCommand::getRequestedAmount)
                .toList();

        payAmountPolicy.validatePayAmount(command.getTotalAmount(), targetAmounts);
    }

    private CreatePayInDiscordCommand mapToDiscordCommand(CreatePayCommand command) {
        List<TargetOfCreatePayInDiscordCommand> discordTargets = command.getTargets().stream()
                .map(target -> TargetOfCreatePayInDiscordCommand.create(target.getTargetMemberId(), target.getRequestedAmount()))
                .toList();

        return CreatePayInDiscordCommand.builder()
                .payCreatorId(command.getPayCreatorId())
                .totalAmount(command.getTotalAmount())
                .bank(command.getBank())
                .targets(discordTargets)
                .totalTargetNum(command.getTotalTargetNum())
                .payType(command.getPayType())
                .build();
    }

    private PayRequest savePayRequest(CreatePayCommand command, Long discordIdForPay) {
        PayRequest payRequest = command.toDomainEntity(discordIdForPay);
        return createPayPort.createPayRequest(payRequest);
    }

    private void savePayRequestTargets(CreatePayCommand command, PayRequest payRequest) {
        List<PayRequestTarget> payRequestTargets = command.getTargets().stream()
                .map(target -> PayRequestTarget.withoutId(
                        target.getTargetMemberId(),
                        payRequest.getId(),
                        target.getRequestedAmount()
                ))
                .toList();

        createPayPort.createPayRequestTargets(payRequestTargets);
    }
}
