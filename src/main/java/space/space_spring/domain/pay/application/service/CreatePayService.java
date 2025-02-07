package space.space_spring.domain.pay.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.discord.application.port.out.CreatePayInDiscordPort;
import space.space_spring.domain.discord.application.port.out.CreatePayMessageCommand;
import space.space_spring.domain.discord.application.port.out.TargetOfCreatePayMessageCommand;
import space.space_spring.domain.pay.application.port.in.createPay.CreatePayCommand;
import space.space_spring.domain.pay.application.port.in.createPay.CreatePayUseCase;
import space.space_spring.domain.pay.application.port.in.createPay.TargetOfCreatePayCommand;
import space.space_spring.domain.pay.application.port.out.CreatePayPort;
import space.space_spring.domain.pay.domain.PayRequest;
import space.space_spring.domain.pay.domain.PayRequestTarget;
import space.space_spring.domain.spaceMember.LoadSpaceMemberPort;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.pay.domain.PayAmountPolicy;
import space.space_spring.domain.spaceMember.SpaceMember;
import space.space_spring.domain.spaceMember.SpaceMembers;
import space.space_spring.global.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.PAY_CREATOR_AND_TARGETS_ARE_NOT_IN_SAME_SPACE;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreatePayService implements CreatePayUseCase {

    private final CreatePayPort createPayPort;
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final CreatePayInDiscordPort createPayInDiscordPort;

    @Override
    @Transactional
    public Long createPay(CreatePayCommand command) {
        // 정산 생성자, 정산 대상자들이 같은 스페이스에 존재하는지 검증
        SpaceMember payCreator = loadPayCreator(command.getPayCreatorId());
        List<SpaceMember> targetMembers = loadTargetMembers(command.getTargets());
        validateSpaceMembers(payCreator, targetMembers);

        // PayAmountPolicy 유효성 검증
        validatePayAmountPolicy(command);

        // 디스코드로 보내기
        Long discordIdForPay = createPayInDiscordPort.createPayMessage(createCommandForDiscord(command, payCreator, targetMembers));

        // PayRequest 도메인 엔티티 생성 & 저장
        PayRequest savedPayRequest = savePayRequest(command, discordIdForPay);

        // PayRequestTarget 도메인 엔티티들 생성 & 저장
        savePayRequestTargets(command, savedPayRequest);

        return savedPayRequest.getId();
    }

    private SpaceMember loadPayCreator(Long payCreatorId) {
        return loadSpaceMemberPort.loadSpaceMemberById(payCreatorId);
    }

    private List<SpaceMember> loadTargetMembers(List<TargetOfCreatePayCommand> targetCommands) {
        return targetCommands.stream()
                .map(target -> loadSpaceMemberPort.loadSpaceMemberById(target.getTargetMemberId()))
                .toList();
    }

    private void validateSpaceMembers(SpaceMember payCreator, List<SpaceMember> payRequestTargets) {
        List<SpaceMember> allOfSpaceMember = new ArrayList<>();
        allOfSpaceMember.add(payCreator);
        allOfSpaceMember.addAll(payRequestTargets);
        SpaceMembers spaceMembers = SpaceMembers.of(allOfSpaceMember);
        try {
            spaceMembers.validateMembersInSameSpace();
        } catch (IllegalStateException e) {
            throw new CustomException(PAY_CREATOR_AND_TARGETS_ARE_NOT_IN_SAME_SPACE);           // 이 방식 괜찮은지 ??
        }
    }

    private void validatePayAmountPolicy(CreatePayCommand command) {
        PayAmountPolicy payAmountPolicy = command.getPayType().getPayAmountPolicy();

        List<Money> targetAmounts = command.getTargets().stream()
                .map(TargetOfCreatePayCommand::getRequestedAmount)
                .toList();

        payAmountPolicy.validatePayAmount(command.getTotalAmount(), targetAmounts);
    }

    private CreatePayMessageCommand createCommandForDiscord(CreatePayCommand command, SpaceMember payCreator, List<SpaceMember> targetMembers) {
        List<TargetOfCreatePayMessageCommand> targetsForDiscord = command.getTargets().stream()
                .map(targetCommand -> {
                    SpaceMember matchedMember = targetMembers.stream()
                            .filter(member -> member.getId().equals(targetCommand.getTargetMemberId()))
                            .findFirst()
                            // 혹시라도 매칭되는 멤버가 없다면 예외를 발생시켜 문제를 조기에 파악할 수 있음
                            .orElseThrow(() -> new IllegalArgumentException(
                                    "해당 ID를 가진 SpaceMember를 찾을 수 없습니다. targetMemberId: " + targetCommand.getTargetMemberId()
                            ));     // 이 방식 괜찮은지 ??

                    return TargetOfCreatePayMessageCommand.create(
                            matchedMember.getDiscordId(),
                            targetCommand.getRequestedAmount());
                })
                .toList();

        return CreatePayMessageCommand.builder()
                .payCreatorDiscordId(payCreator.getDiscordId())
                .totalAmount(command.getTotalAmount())
                .bank(command.getBank())
                .targets(targetsForDiscord)
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
