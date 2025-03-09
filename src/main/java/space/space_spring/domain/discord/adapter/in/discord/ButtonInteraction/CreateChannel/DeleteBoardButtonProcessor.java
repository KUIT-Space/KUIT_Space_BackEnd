package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.CreateChannel;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonInteractionProcessor;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonUtil;
import space.space_spring.domain.post.application.port.in.boardCache.LoadBoardCacheUseCase;
import space.space_spring.domain.post.application.port.in.deleteBoard.DeleteBoardUseCase;
import space.space_spring.domain.post.application.port.in.loadBoard.LoadBoardUseCase;
import space.space_spring.domain.space.application.port.in.LoadSpaceUseCase;
import space.space_spring.global.exception.CustomException;

import java.util.List;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.BOARD_IS_NOT_IN_SPACE;

@Component
@RequiredArgsConstructor
public class DeleteBoardButtonProcessor implements ButtonInteractionProcessor {

    private final LoadSpaceUseCase loadSpaceUseCase;
    private final LoadBoardUseCase loadBoardUseCase;
    private final DeleteBoardUseCase deleteBoardUseCase;
    @Override
    public boolean supports(String buttonId){
        if (buttonId.startsWith("delete-channel")){
            return true;
        }
        return false;
    }

    @Override
    public void process(ButtonInteractionEvent event){
        String buttonId = event.getComponentId();
        String[] parts = buttonId.split(":");
        if (parts.length < 3) return;


        Long channelId = Long.parseLong(parts[1]);
        Long boardId = Long.parseLong(parts[2]);

        Long guildId  =event.getGuild().getIdLong();

        validateBoardInSpace(guildId, boardId);
        if(deleteBoard(boardId) ){
            event.reply("삭제 완료")
                    .queue();
        }

        event.reply("채널 삭제에 문제가 발생했습니다")
                .queue();

    }

    private boolean deleteBoard(Long boardId){
        return deleteBoardUseCase.delete(boardId);
    }

    private void validateBoardInSpace(Long guildId, Long boardId){
        Long spaceId=loadSpaceUseCase.loadByDiscordId(guildId).getId();

        if(loadBoardUseCase.findById(boardId).getSpaceId().equals(spaceId)){
            return;

        }
        throw new CustomException(BOARD_IS_NOT_IN_SPACE);
    }

}
