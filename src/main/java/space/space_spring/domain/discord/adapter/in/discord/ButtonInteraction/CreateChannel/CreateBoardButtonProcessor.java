package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.CreateChannel;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonInteractionProcessor;

import space.space_spring.domain.discord.adapter.out.DiscordWebHookAdapter;
import space.space_spring.domain.discord.application.port.out.WebHookPort;

import space.space_spring.domain.discord.adapter.in.discord.DiscordUtil;

import space.space_spring.domain.discord.domain.ChannelCommand;
import space.space_spring.domain.discord.domain.DiscordTags;
import space.space_spring.domain.post.application.port.in.boardCache.LoadBoardCacheUseCase;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardUseCase;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.domain.post.domain.Tag;
import space.space_spring.domain.space.application.port.in.LoadSpaceUseCase;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;

import java.util.Optional;

import java.util.List;

import static space.space_spring.domain.post.domain.BoardType.*;

@Component
@RequiredArgsConstructor
@Transactional
public class CreateBoardButtonProcessor implements ButtonInteractionProcessor {
    private final CreateBoardUseCase createBoardUseCase;
    private final LoadBoardCacheUseCase loadBoardCacheUseCase;
    private final LoadSpaceUseCase loadSpaceUseCase;
    private final WebHookPort webHookPort;
    private final DiscordUtil discordUtil;

    @Override
    public boolean supports(String buttonId){
        if (buttonId.startsWith("create-channel:")){
            return true;
        }
        return false;
    }


    @Override
    public void process(ButtonInteractionEvent event){
        String buttonId = event.getComponentId();
        String[] parts = buttonId.split(":");
        if (parts.length < 3) return;

        String menuType = parts[1];
        String targetChannelIdStr = parts[2];
        Long targetChannelId = Long.parseLong(targetChannelIdStr);
        String channelName = parts[3];

        Guild guild  =event.getGuild();
        GuildChannel guildChannel = event.getGuild().getGuildChannelById(targetChannelId);
        //Todo nullpoint exception -> now space init yet
        Long spaceId=loadSpaceUseCase.loadByDiscordId(event.getGuild().getIdLong()).getId();

        //String channelName = guildChannel.getName();
        ChannelCommand command=ChannelCommand.builder()
                .channelDiscordId(targetChannelId)
                .channelName(channelName)

                .webhookUrl(webHookPort.getOrCreate(targetChannelId))

                .spaceId(spaceId)
                .tags(getTags(event.getJDA(),targetChannelId))
                .build();

        if(isExistBoard(targetChannelId)){
            event.reply("이미 게시판으로 등록된 채널입니다")
                    .setEphemeral(true).queue();
            return;
        }

        switch (menuType.toLowerCase()) {

            case "pay-board":
                createPayBoard(command);
                break;

        }
        switch (BoardType.fromString(menuType)){
            case POST:
                createBoard(command);
                break;
            case QUESTION:
                createQuestionBoard(command);
                break;
            case PAY:
                createPayBoard(command);
                break;
            case TIP:
                createTipBoard(command);
                break;
            case NOTICE:
                createNoticeBoard(command);
                break;
            case SEASON_NOTICE:
                createSeasonNoticeBoard(command);
                break;
            default:
                event.reply("잘못된 요청입니다.").setEphemeral(true).queue();
                return;
        }

        event.reply("`" + menuType + "` 기능이 `" + targetChannelIdStr + "` 채널에서 실행되었습니다.")
                .setEphemeral(true).queue();

    }

    private void createBoard(ChannelCommand command) {
        System.out.println("createBoard 호출: 채널 ID - " + command.getChannelName());
        // Todo 중복 저장 검사
        createBoardUseCase.createBoard(command.getCreateBoardCommand(POST));
    }
    private void createNoticeBoard(ChannelCommand command) {
        System.out.println("createBoard 호출: 채널 ID - " + command.getChannelName());
        // Todo 중복 저장 검사
        createBoardUseCase.createBoard(command.getCreateBoardCommand(NOTICE));
    }
    private void createSeasonNoticeBoard(ChannelCommand command) {
        System.out.println("createBoard 호출: 채널 ID - " + command.getChannelName());
        // Todo 중복 저장 검사
        createBoardUseCase.createBoard(command.getCreateBoardCommand(SEASON_NOTICE));
    }

    private void createTipBoard(ChannelCommand command) {
        System.out.println("createBoard 호출: 채널 ID - " + command.getChannelName());
        // Todo 중복 저장 검사
        createBoardUseCase.createBoard(command.getCreateBoardCommand(TIP));
    }

    private void createPayBoard(ChannelCommand command) {
        System.out.println("createPayBoard 호출: 채널 ID - " + command.getChannelName());
        // 구현 로직 추가
        createBoardUseCase.createBoard(command.getCreateBoardCommand(PAY));
    }

    private void createQuestionBoard(ChannelCommand command) {
        System.out.println("createQuestionBoard 호출: 채널 ID - " + command.getChannelName());
        // Todo 중복 저장 검사
        createBoardUseCase.createBoard(command.getCreateBoardCommand(BoardType.QUESTION));
    }




    private boolean isExistBoard(Long channelId){
        return loadBoardCacheUseCase.findByDiscordId(channelId).isPresent();
    }

    private DiscordTags getTags(JDA jda,Long channelId){
        if(discordUtil.isForumChannel(channelId)) {
            return DiscordTags.from(jda.getForumChannelById(channelId).getAvailableTags());
        }
        return DiscordTags.empty();
    }


}
