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
import space.space_spring.domain.post.domain.BoardType;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DeleteBoardButtonProcessor implements ButtonInteractionProcessor {
    private final LoadBoardCacheUseCase loadBoardCacheUseCase;
    private final ButtonUtil buttonUtil;
    @Override
    public boolean supports(String buttonId){
        if (buttonId.startsWith("delete-board")){
             return true;
        }
        return false;
    }

    @Override
    public void process(ButtonInteractionEvent event){

        String buttonId = event.getComponentId();
        Guild guild = event.getGuild();
        // Todo 현재 등록된 게시판 전부 가져오기
        // 길드의 모든 텍스트 채널을 버튼으로 생성
        List<Long> boardList = loadBoardCacheUseCase.findAllChannel();

        List<GuildChannel> channels = guild.getChannels();
        List<Button> channelButtons = channels.stream()
                .filter(channel->channel.getType()== ChannelType.FORUM||channel.getType()==ChannelType.TEXT)
                //현재 등록된 채널만 버튼으로
                .filter(channel->loadBoardCacheUseCase.findByDiscordId(channel.getIdLong()).isPresent())
                .map(channel -> Button.secondary("check:delete-channel:"  + channel.getId()+":"+channel.getName(), channel.getName()))
                .collect(Collectors.toList());

        // Discord API 제한: 한 번에 최대 5개의 버튼만 지원되므로 여러 줄로 나눔
        List<ActionRow> rows = buttonUtil.partitionButtons(channelButtons);

        event.reply("삭제할 채널을 선택하세요")
                .addComponents(rows)
                .queue();

    }
}
