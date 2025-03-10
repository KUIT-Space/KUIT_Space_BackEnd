package space.space_spring.domain.discord.adapter.in.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;
import space.space_spring.domain.pay.application.port.in.completePay.CompletePayUseCase;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestPort;
import space.space_spring.domain.pay.application.port.out.LoadPayRequestTargetPort;
import space.space_spring.domain.post.application.port.in.boardCache.LoadBoardCacheUseCase;
import space.space_spring.domain.post.application.port.in.loadBoard.LoadBoardUseCase;
import space.space_spring.domain.post.application.port.out.LoadBoardCachePort;
import space.space_spring.domain.post.application.service.LoadBoardService;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.domain.space.application.port.in.LoadSpaceUseCase;
import space.space_spring.domain.spaceMember.application.port.out.LoadSpaceMemberPort;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MessageReactionEventListener extends ListenerAdapter {
    //private final Like
    private final DiscordUtil discordUtil;
    private final LoadBoardCacheUseCase loadBoardCacheUseCase;
    private final LoadBoardUseCase loadBoardUseCase;
    private final CompletePayUseCase completePayUseCase;
    private final LoadSpaceUseCase loadSpaceUseCase;
    private final LoadSpaceMemberPort loadSpaceMemberPort;
    private final LoadPayRequestTargetPort loadPayRequestTargetPort;
    private final LoadPayRequestPort loadPayRequestPort;
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){


        Optional<Long> boardId = getBoardId(event);
        if(boardId.isEmpty()){
            return;
        }

        Long guildId=event.getGuild().getIdLong();
        Long messageId = event.getMessageIdLong();
        Long memberId = event.getMember().getIdLong();
        Long spaceMemberId = loadSpaceMemberPort.loadByDiscord(guildId,event.getMessageIdLong()).getId();

        if(isPayBoard(boardId.get())){
            //Todo 정산 완료 UseCase
            completePayUseCase.completeForRequestedPay(
                    spaceMemberId,
            loadPayRequestTargetPort.loadByTargetMemberId(spaceMemberId).stream()
                    .filter(payTarget-> {
                        return loadPayRequestPort.loadById(payTarget.getPayRequestId()).getDiscordMessageId().equals(event.getMessageIdLong());
                    }).findFirst().orElseThrow().getId());
        }


        //Todo 좋아요 UseCase 호출

    }

    @Override
    public void onMessageReactionRemove(MessageReactionRemoveEvent event){


        Optional<Long> boardId = getBoardId(event);
        if(boardId.isEmpty()){
            return;
        }


        Long guildId=event.getGuild().getIdLong();
        Long messageId = event.getMessageIdLong();
        Long memberId = event.getMember().getIdLong();

        if(isPayBoard(boardId.get())){
            //Todo 정산 완료 취소 UseCase
        }


        //Todo 좋아요  삭제하는 UseCase 호출
    }

    private Optional<Long> getBoardId(GenericMessageEvent event){
        MessageChannelUnion channel=event.getChannel();
        Long parentChannelId = discordUtil.getRootChannelId(channel);
        //log.info("parentChannelId:"+parentChannelId);

        return loadBoardCacheUseCase.findByDiscordId(parentChannelId);

    }

    private boolean isPayBoard(Long boardId){
        return loadBoardUseCase.getBoardTypeById(boardId).equals(BoardType.PAY);
    }
}
