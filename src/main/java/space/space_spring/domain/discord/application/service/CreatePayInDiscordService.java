package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadPort;
import space.space_spring.domain.discord.application.port.out.CreateDiscordWebHookMessageCommand;
import space.space_spring.domain.discord.domain.DiscordPayMessage;
import space.space_spring.domain.pay.application.port.out.LoadPayBoardPort;
import space.space_spring.domain.pay.domain.Money;

import space.space_spring.domain.discord.application.port.in.createPay.CreatePayInDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.createPay.CreatePayInDiscordCommand;

import space.space_spring.domain.post.application.port.in.loadBoard.LoadBoardUseCase;
import space.space_spring.domain.post.application.service.BoardCacheService;
import space.space_spring.domain.space.application.port.in.LoadSpaceUseCase;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;
import space.space_spring.domain.spaceMember.application.port.out.NicknameAndProfileImage;
import space.space_spring.domain.spaceMember.domian.SpaceMember;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.BOARD_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.DISCORD_THREAD_CREATE_FAIL;

@Service
@RequiredArgsConstructor
public class CreatePayInDiscordService implements CreatePayInDiscordUseCase {

    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final CreateDiscordThreadPort createDiscordThreadPort;
    private final LoadPayBoardPort loadPayBoardPort;
    private final LoadBoardUseCase loadBoardUseCase;
    private final LoadSpaceUseCase loadSpaceUseCase;
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

        Long channelDiscordId = loadPayBoardPort.getPayBoardDiscordId(payCreator.getSpaceId());
        return DiscordPayMessage.builder()
                .creator(NicknameAndProfileImage.of(payCreator.getNickname(), payCreator.getProfileImageUrl()))
                .payTitle("정산을 시작합니다")
                .payType(command.getPayType())
                .Bank(command.getBank().getName())
                .accountNumber(command.getBank().getAccountNumber())
                .channelDiscordId(channelDiscordId)
                .targetMembers(messageTargets)
                .channelWebhook(loadBoardUseCase.findByDiscordId(channelDiscordId).orElseThrow(()->{
                    throw new CustomException(BOARD_NOT_FOUND);
                }).getWebhookUrl())
                .guildDiscordId(loadSpaceUseCase.load(payCreator.getSpaceId()).getDiscordId())
                .total(command.getTotalAmount())
                .build();
    }
}
