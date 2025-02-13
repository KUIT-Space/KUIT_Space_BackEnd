package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.createPay.CreatePayInDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.createPay.CreatePayInDiscordCommand;
import space.space_spring.domain.discord.application.port.out.CreatePayMessageCommand;
import space.space_spring.domain.discord.application.port.out.CreatePayMessagePort;
import space.space_spring.domain.discord.application.port.out.TargetOfCreatePayMessageCommand;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreatePayInDiscordService implements CreatePayInDiscordUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final CreatePayMessagePort createPayMessagePort;

    @Override
    public Long createPayInDiscord(CreatePayInDiscordCommand command) {
        return createPayMessagePort.createPayMessage(mapToPayMessageCommand(command));
    }

    private CreatePayMessageCommand mapToPayMessageCommand(CreatePayInDiscordCommand command) {
        // 정산 생성자의 Discord ID 조회
        SpaceMember payCreator = loadSpaceMemberPort.loadById(command.getPayCreatorId());
        Long payCreatorDiscordId = payCreator.getDiscordId();

        // 각 정산 대상자에 대해 Discord ID를 조회하여 TargetOfCreatePayMessageCommand로 변환
        List<TargetOfCreatePayMessageCommand> messageTargets = command.getTargets().stream()
                .map(target -> {
                    SpaceMember targetMember = loadSpaceMemberPort.loadById(target.getTargetMemberId());
                    Long targetMemberDiscordId = targetMember.getDiscordId();
                    return TargetOfCreatePayMessageCommand.create(targetMemberDiscordId, target.getRequestedAmount());
                })
                .toList();

        return CreatePayMessageCommand.builder()
                .payCreatorDiscordId(payCreatorDiscordId)
                .totalAmount(command.getTotalAmount())
                .bank(command.getBank())
                .targets(messageTargets)
                .totalTargetNum(command.getTotalTargetNum())
                .payType(command.getPayType())
                .build();
    }
}
