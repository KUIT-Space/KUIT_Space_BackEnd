package space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.CreateChannel;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonInteractionProcessor;
import space.space_spring.domain.discord.adapter.in.discord.ButtonInteraction.ButtonUtil;
import space.space_spring.domain.post.domain.BoardType;

import java.util.List;
import java.util.stream.Collectors;
@Component
@RequiredArgsConstructor
public class CategoryButtonProcessor implements ButtonInteractionProcessor {
    private final ButtonUtil buttonUtil;
    @Override
    public boolean supports(String buttonId){
        if (buttonId.startsWith("channel-category:")){
            return true;
        }
        return false;
    }

    @Override
    public void process(ButtonInteractionEvent event) {
        String buttonId = event.getComponentId();
        String boardTypeString = buttonId.split(":")[1];
        Guild guild = event.getGuild();
        if (guild == null) {
            event.reply("길드에서만 사용 가능합니다.").setEphemeral(true).queue();
            return;
        }

        // 길드의 모든 텍스트 채널을 버튼으로 생성
        List<Category> categories = guild.getCategories();
        List<Button> channelButtons = categories.stream()
                //.filter(channel->channel.getType()== ChannelType.FORUM||channel.getType()==ChannelType.TEXT)
                .map(category -> Button.secondary("create-board:" + boardTypeString + ":" + category.getId()+":"+category.getName(), category.getName()))
                .collect(Collectors.toList());

        // Discord API 제한: 한 번에 최대 5개의 버튼만 지원되므로 여러 줄로 나눔
        List<ActionRow> rows = buttonUtil.partitionButtons(channelButtons);

        BoardType boardType= BoardType.fromString(boardTypeString);
        event.reply(boardType.getKrName()+"으로 사용할 채널을 선택할 카테고리를 선택해주세요."
                        +"\n버튼을 누르면 해당 카테고리에 속하는 채널들을 선택할 수 있습니다.")
                .addComponents(rows)
                .queue();
    }
}
