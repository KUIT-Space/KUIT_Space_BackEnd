package space.space_spring.domain.discord.adapter.out.EditDiscordMessage;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.dv8tion.jda.api.entities.channel.forums.ForumTag;
import net.dv8tion.jda.api.managers.channel.concrete.ThreadChannelManager;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.DiscordUtil;
import space.space_spring.domain.discord.application.port.out.updateMessage.UpdateMessageInDiscordPort;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EditDiscordMessageAdapter implements UpdateMessageInDiscordPort {
    private final JDA jda;
    private final DiscordUtil discordUtil;

    public void editMessage(String webHookUrl , Long boardDiscordId,Long messageDiscordId, String title,String content, List<Long> tags){
        WebhookClient.createClient(jda,webHookUrl).editMessageById(messageDiscordId,content)

                .queue(m->{

                    if(discordUtil.isForumChannel(boardDiscordId)){
                        ThreadChannelManager manager = jda.getThreadChannelById(messageDiscordId).getManager();
                        if(m.getChannel().getIdLong()==messageDiscordId){
                            manager.setName(title).queue();
                        }
                        List<ForumTag> allTags = jda.getForumChannelById(messageDiscordId).getAvailableTags();

                        manager.setAppliedTags(
                                allTags.stream().filter(tag->tags.contains(tag.getIdLong()))
                                        .toList()).complete();

                    }

                    }
                );
    }
}
