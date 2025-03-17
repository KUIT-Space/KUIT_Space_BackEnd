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
import space.space_spring.domain.post.domain.BoardType;

import java.util.List;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class CreateChannelButtonProcessor implements ButtonInteractionProcessor {
    private final LoadBoardCacheUseCase loadBoardCacheUseCase;
    private final ButtonUtil buttonUtil;
    @Override
    public boolean supports(String buttonLabel){
        if(buttonLabel.startsWith("create-board:")){
            return true;
        }
        return false;
    }

    @Override
    public void process(ButtonInteractionEvent event){
        String buttonId = event.getComponentId();
        String boardTypeString = buttonId.split(":")[1];
        String categoryId = buttonId.split(":")[2];
        String categoryName = buttonId.split(":")[3];
        Guild guild = event.getGuild();
        if (guild == null) {
            event.reply("길드에서만 사용 가능합니다.").setEphemeral(true).queue();
            return;
        }

        // 길드의 모든 텍스트 채널을 버튼으로 생성
        List<GuildChannel> channels = guild.getCategoryById(categoryId).getChannels();

        List<Button> channelButtons = channels.stream()
                .filter(channel->channel.getType()== ChannelType.FORUM||channel.getType()==ChannelType.TEXT)
                //ToDo 이미 등록한 게시판 제외
                .filter(channel->loadBoardCacheUseCase.findByDiscordId(channel.getIdLong()).isEmpty())
                .map(channel -> Button.secondary("check:create-channel:" + boardTypeString + ":" + channel.getId()+":"+channel.getName(), channel.getName()))
                .collect(Collectors.toList());
        if(channelButtons==null||channelButtons.isEmpty()){
            event.reply("해당 카테고리에는 TEXT,FORUM 이고 게시판으로 등록되지 않은 채널이 없습니다.").setEphemeral(true).queue();
            return;
        }
        // Discord API 제한: 한 번에 최대 5개의 버튼만 지원되므로 여러 줄로 나눔
        List<ActionRow> rows = buttonUtil.partitionButtons(channelButtons);

        BoardType boardType= BoardType.fromString(boardTypeString);
        event.reply(boardType.getKrName()+"으로 사용할 채널을 선택하세요"+"\n"+boardType.getDetail()+
                        "\n\n"+categoryName)
                .addComponents(rows)
                .queue();


    }

}
