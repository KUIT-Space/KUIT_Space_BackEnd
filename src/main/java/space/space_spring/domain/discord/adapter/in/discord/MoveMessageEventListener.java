package space.space_spring.domain.discord.adapter.in.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonUtil;
import space.space_spring.domain.discord.application.port.in.discord.InputMessageFromDiscordUseCase;
import space.space_spring.domain.post.application.port.in.boardCache.LoadBoardCacheUseCase;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostUseCase;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MoveMessageEventListener extends ListenerAdapter {
    private final LoadBoardPort loadBoardPort;
    private final CreatePostUseCase createPostUseCase;
    private final InputMessageFromDiscordUseCase inputMessageFromDiscordUseCase;
    private final LoadBoardCacheUseCase loadBoardCacheUseCase;
    private final ButtonUtil buttonUtil;
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;
        if(!event.getName().equals("move-message")){
            return;
        }

        //Todo redis cache 로 바꾸기
        Guild guild = event.getGuild();
        // Todo 현재 등록된 게시판 전부 가져오기
        // 길드의 모든 텍스트 채널을 버튼으로 생성
        List<Long> boardList = loadBoardCacheUseCase.findAllChannel();

        List<GuildChannel> channels = guild.getChannels();
        List<Button> channelButtons = channels.stream()
                .filter(channel->channel.getType()== ChannelType.FORUM||channel.getType()==ChannelType.TEXT)
                //현재 등록된 채널만 버튼으로
                .filter(channel->loadBoardCacheUseCase.findByDiscordId(channel.getIdLong()).isPresent())
                .map(channel -> Button.secondary("check:move-message:"  + channel.getId()+":"+loadBoardCacheUseCase.findByDiscordId(channel.getIdLong()).get()+":"+channel.getName(), channel.getName()))
                .collect(Collectors.toList());

        if(channelButtons.isEmpty()||channelButtons==null){
            event.reply("현재 등록된 게시판이 없습니다").queue();
            return;
        }

        // Discord API 제한: 한 번에 최대 5개의 버튼만 지원되므로 여러 줄로 나눔
        List<ActionRow> rows = buttonUtil.partitionButtons(channelButtons);

        event.reply("이 채널의 글을 이동 시킬 게시판(채널)을 선택 해주세요"+
                        "\n이 채널의 메세지가 디스코드와 space web 모두 메세지가 복사됩니다.")
                .setEphemeral(true)
                .addComponents(rows)
                .queue();





    }


    //성공 여부 반환
    private boolean moveChannel(){
        return true;
    }

    //몇개를 저장 성공했는지 반환
    private int savePost(){

        return 0;
    }
}
