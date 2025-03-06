package space.space_spring.domain.discord.adapter.in.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.domain.ChannelCommand;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardCommand;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardUseCase;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.domain.space.application.port.in.LoadSpaceUseCase;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static space.space_spring.domain.post.domain.BoardType.PAY;
import static space.space_spring.domain.post.domain.BoardType.POST;

@Component
@RequiredArgsConstructor
public class ChannelSettingEventListener extends ListenerAdapter {
    private final CreateBoardUseCase createBoardUseCase;
    private final LoadSpaceUseCase loadSpaceUseCase;


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("set-board")) {
            event.reply("설정하고 싶은 게시판 종류를 선택하세요.(포럼과 텍스트 채널만 가능)")
                    .addActionRow(
                        getChannelSettingButtons()
                    ).queue();
        }
    }


    private List<Button> getChannelSettingButtons(){
        List<Button> buttons = new ArrayList<>();
        for(BoardType boardType:BoardType.values()){
            buttons.add(Button.primary("create-board:"+boardType.getName(),
                    boardType.getKrName()));
        }

//        buttons.add(Button.primary("create-board:payboard", "정산-안내게시판"));
        buttons.add(Button.primary("delete-board:", "게시판-삭제"));
        return buttons;

    }
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();

        // 첫 번째 메뉴 선택 (board, payboard, question-board)
        if (buttonId.startsWith("create-board:")) {
            String boardTypeString = buttonId.split(":")[1];
            Guild guild = event.getGuild();
            if (guild == null) {
                event.reply("길드에서만 사용 가능합니다.").setEphemeral(true).queue();
                return;
            }

            // 길드의 모든 텍스트 채널을 버튼으로 생성
            List<GuildChannel> channels = guild.getChannels();
            List<Button> channelButtons = channels.stream()
                    .filter(channel->channel.getType()== ChannelType.FORUM||channel.getType()==ChannelType.TEXT)
                    //ToDo 이미 등록한 게시판 제외
                    .map(channel -> Button.secondary("check:create-channel:" + boardTypeString + ":" + channel.getId()+":"+channel.getName(), channel.getName()))
                    .collect(Collectors.toList());

            // Discord API 제한: 한 번에 최대 5개의 버튼만 지원되므로 여러 줄로 나눔
            List<ActionRow> rows = partitionButtons(channelButtons);

            BoardType boardType= BoardType.fromString(boardTypeString);
            event.reply(boardType.getKrName()+"으로 사용할 채널을 선택하세요"+"\n"+boardType.getDetail())
                    .addComponents(rows)
                    .queue();


        }
        else if (buttonId.startsWith("check:")) {
            String[] parts = buttonId.split(":");
            if (parts.length < 4) return;

            String channelName = parts[4];
            String menuType = parts[2];
            int index = buttonId.indexOf(":"); // 첫 번째 ":"의 위치 찾기
            String returnString = (index != -1) ? buttonId.substring(index + 1) : buttonId; // ":" 이후 부분 반환


            event.reply("**"+channelName+"**"+
                            "이 채널을 **"+menuType+"**으로 등록하시겠습니까?")
                    .addActionRow(
                            Button.primary(returnString, " 예 "),
                            Button.primary("cancel", "아니오")
                    ).queue();


        }
        // 채널 선택 시 최종 함수 호출
        else if (buttonId.startsWith("create-channel:")) {
            String[] parts = buttonId.split(":");
            if (parts.length < 3) return;

            String menuType = parts[1];
            String channelId = parts[2];
            String channelName = parts[3];

            Guild guild  =event.getGuild();
            GuildChannel guildChannel = event.getGuild().getGuildChannelById(channelId);
            //Todo nullpoint exception -> now space init yet
            Long spaceId=loadSpaceUseCase.loadByDiscordId(event.getGuild().getIdLong()).getId();

            //String channelName = guildChannel.getName();
            ChannelCommand command=ChannelCommand.builder()
                    .channelDiscordId(Long.parseLong(channelId))
                    .channelName(channelName)
                    .webhookUrl(getWebHookUrl(guild,channelId))
                    .spaceId(spaceId)
                    .build();

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
                default:
                    event.reply("잘못된 요청입니다.").setEphemeral(true).queue();
                    return;
            }

            event.reply("`" + menuType + "` 기능이 `" + channelId + "` 채널에서 실행되었습니다.").queue();
        }


        else if (buttonId.startsWith("delete-board")){
            // Todo 현재 등록된 게시판 전부 가져오기



        }
    }

    private List<ActionRow> partitionButtons(List<Button> buttons) {
        final int MAX_BUTTONS_PER_ROW = 5;
        List<ActionRow> rows = new java.util.ArrayList<>();
        for (int i = 0; i < buttons.size(); i += MAX_BUTTONS_PER_ROW) {
            rows.add(ActionRow.of(buttons.subList(i, Math.min(i + MAX_BUTTONS_PER_ROW, buttons.size()))));
        }
        return rows;
    }

    private void createBoard(ChannelCommand command) {
        System.out.println("createBoard 호출: 채널 ID - " + command.getChannelName());
        // Todo 중복 저장 검사
        createBoardUseCase.createBoard(command.getCreateBoardCommand(POST));
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

    //ToDo Webhook adapter 로 책임 분리 예정
    private String getWebHookUrl(Guild guild,String channelId){
        TextChannel textChannel = guild.getChannelById(TextChannel.class,channelId);
        if(textChannel!=null){
            return textChannel.createWebhook("space-webhook").complete().getUrl();
        }
        ForumChannel forumChannel = guild.getChannelById(ForumChannel.class,channelId);
        if(forumChannel !=null){
            return forumChannel.createWebhook("space-webhook").complete().getUrl();
        }
        return null;
    }

}
