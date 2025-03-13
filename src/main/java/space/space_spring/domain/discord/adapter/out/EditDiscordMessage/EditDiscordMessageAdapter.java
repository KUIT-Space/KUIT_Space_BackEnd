package space.space_spring.domain.discord.adapter.out.EditDiscordMessage;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.WebhookClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EditDiscordMessageAdapter {
    private final JDA jda;


    public void editMessage(String webHookUrl , Long messageDiscordId, String content, List<Long> tags){
        WebhookClient.createClient(jda,webHookUrl).editMessageById(messageDiscordId,content)
                .queue(()->{
                    
                });
    }
}
