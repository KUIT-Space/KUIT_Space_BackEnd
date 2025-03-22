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
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonInteractionProcessor;
import space.space_spring.domain.discord.domain.ChannelCommand;
import space.space_spring.domain.post.application.port.in.boardCache.LoadBoardCacheUseCase;
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

    private final List<ButtonInteractionProcessor> buttonInteractionProcessors;


    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("set-board")) {
            event.reply("설정하고 싶은 게시판 종류를 선택하세요.(포럼과 텍스트 채널만 가능)")
                    .addComponents(
                            partitionButtons(getChannelSettingButtons())
                    )
                    .setEphemeral(true)
                    .queue();
        }
    }


    private List<Button> getChannelSettingButtons(){
        List<Button> buttons = new ArrayList<>();
        for(BoardType boardType:BoardType.values()){
            buttons.add(Button.primary("channel-category:"+boardType.getName(),
                    boardType.getKrName()));
        }

//        buttons.add(Button.primary("create-board:payboard", "정산-안내게시판"));
        buttons.add(Button.primary("delete-board:", "게시판-삭제"));
        return buttons;

    }
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();
        boolean hasButton=false;
        for(ButtonInteractionProcessor processor:buttonInteractionProcessors){
            if(processor.supports(buttonId)){
                hasButton=true;
                processor.process(event);
                return;
            }
            //System.out.println("button processor work");
        }

        if(!hasButton) {
            event.reply("알수 없는 버튼 요청입니다.(input="+buttonId+")").setEphemeral(true).queue();
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




}
