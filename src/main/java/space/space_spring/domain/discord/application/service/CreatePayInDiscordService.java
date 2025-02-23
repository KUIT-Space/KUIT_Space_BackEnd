package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.CreatePayInDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.CreatePayInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.TargetOfCreatePayInDiscordCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadPort;
import space.space_spring.domain.discord.domain.DiscordPayMessage;
import space.space_spring.domain.pay.domain.Money;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.DISCORD_THREAD_CREATE_FAIL;

@Service
@RequiredArgsConstructor
public class CreatePayInDiscordService implements CreatePayInDiscordUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final CreateDiscordThreadPort createDiscordThreadPort;

    @Override
    public Long createPayInDiscord(CreatePayInDiscordCommand command) {
        //String title =" 정산 ";
        try {
            return createDiscordThreadPort.create(mapToPayMessage(command).getPayMessageCommand()).get();
        }catch (Exception e){
            throw new CustomException(DISCORD_THREAD_CREATE_FAIL ,"Exception 발생"+ e.toString());
        }
    }

    private DiscordPayMessage mapToPayMessage(CreatePayInDiscordCommand command) {
        // 정산 생성자의 Discord ID 조회
        SpaceMember payCreator = loadSpaceMemberPort.loadById(command.getPayCreatorId());
        Long payCreatorDiscordId = payCreator.getDiscordId();

        // 각 정산 대상자에 대해 Discord ID를 조회하여 TargetOfCreatePayMessageCommand로 변환
        Map<Long, Money> messageTargets = command.getTargets().stream()
                .collect(Collectors.toMap(
                    target->loadSpaceMemberPort.loadById(target.getTargetMemberId()).getDiscordId(),
                    target->target.getRequestedAmount()
                ));
//                .map(target -> {
//                    SpaceMember targetMember = loadSpaceMemberPort.loadById(target.getTargetMemberId());
//                    Long targetMemberDiscordId = targetMember.getDiscordId();
//                    return TargetOfCreatePayMessageCommand.create(targetMemberDiscordId, target.getRequestedAmount());
//                })

        /*
        Todo 미구현
         */
        return DiscordPayMessage.builder().build();
    }
}
