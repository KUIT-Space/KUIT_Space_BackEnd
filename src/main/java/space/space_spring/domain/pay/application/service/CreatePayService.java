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
        SpaceMember payCreator = loadSpaceMemberPort.loadSpaceMemberById(command.getPayCreatorId());
        List<SpaceMember> targetMembers = new ArrayList<>();
        for (TargetOfCreatePayCommand target : command.getTargets()) {
            targetMembers.add(loadSpaceMemberPort.loadSpaceMemberById(target.getTargetMemberId()));
        }
        validateSpaceMembers(payCreator, targetMembers);

        // PayAmountPolicy 유효성 검증
        validatePayAmountPolicy(command);

        // 디스코드로 보내기
        List<TargetOfCreatePayMessageCommand> targets = new ArrayList<>();
        for (TargetOfCreatePayCommand target : command.getTargets()) {
            SpaceMember targetMember = loadSpaceMemberPort.loadSpaceMemberById(target.getTargetMemberId());
            targets.add(TargetOfCreatePayMessageCommand.create(targetMember.getDiscordId(), target.getRequestedAmount()));
        }

        CreatePayMessageCommand discordCommand = CreatePayMessageCommand.builder()
                .payCreatorDiscordId(payCreator.getDiscordId())
                .totalAmount(command.getTotalAmount())
                .bank(command.getBank())
                .targets(targets)
                .totalTargetNum(command.getTotalTargetNum())
                .payType(command.getPayType())
                .build();

        Long discordIdForPay = createPayInDiscordPort.createPayMessage(discordCommand);

        // PayRequest 도메인 엔티티 생성 & 저장
        PayRequest payRequest = command.toDomainEntity(discordIdForPay);
        PayRequest savedPayRequest = createPayPort.createPayRequest(payRequest);

        // PayRequestTarget 도메인 엔티티들 생성 & 저장
        List<PayRequestTarget> payRequestTargets = createPayRequestTargets(command, savedPayRequest);
        createPayPort.createPayRequestTargets(payRequestTargets);

        return savedPayRequest.getId();
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

        List<Money> targetAmounts = new ArrayList<>();
        for (TargetOfCreatePayCommand target : command.getTargets()) {
            targetAmounts.add(target.getRequestedAmount());
        }
        payAmountPolicy.validatePayAmount(command.getTotalAmount(), targetAmounts);
    }

    private List<PayRequestTarget> createPayRequestTargets(CreatePayCommand command, PayRequest payRequest) {
        List<PayRequestTarget> payRequestTargets = new ArrayList<>();
        for (TargetOfCreatePayCommand target : command.getTargets()) {
            PayRequestTarget payRequestTarget = PayRequestTarget.withoutId(target.getTargetMemberId(), payRequest.getId(), target.getRequestedAmount());
            payRequestTargets.add(payRequestTarget);
        }
        return payRequestTargets;
    }
}
