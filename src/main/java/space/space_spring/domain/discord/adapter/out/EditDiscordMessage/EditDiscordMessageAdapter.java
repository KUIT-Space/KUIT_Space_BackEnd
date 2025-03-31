package space.space_spring.domain.discord.adapter.out.EditDiscordMessage;

import club.minnced.discord.webhook.exception.HttpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.dv8tion.jda.api.entities.channel.forums.ForumTag;
import net.dv8tion.jda.api.managers.channel.concrete.ThreadChannelManager;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.adapter.in.discord.DiscordUtil;
import space.space_spring.domain.discord.application.port.out.updateMessage.UpdateMessageInDiscordPort;
import space.space_spring.global.exception.CustomException;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.NOT_PROVIDE_CROSS_EDIT;

@Component
@RequiredArgsConstructor
@Slf4j
public class EditDiscordMessageAdapter implements UpdateMessageInDiscordPort {
    private final JDA jda;
    private final DiscordUtil discordUtil;

    public void editMessage(String webHookUrl , Long boardDiscordId,Long messageDiscordId, String title,String content, List<Long> tags){

        WebhookClient.createClient(jda,webHookUrl).editMessageById(messageDiscordId,content)

                .queue(m->{

                    if(discordUtil.isForumChannel(boardDiscordId)){
                        ThreadChannelManager manager = jda.getThreadChannelById(messageDiscordId).getManager();
                        if(m.getChannel().getIdLong()==boardDiscordId){
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
    @Override
    public void editThreadMessage(String webHookUrl,Long threadId,Long messageDiscordId,String newContent){
        try {
            club.minnced.discord.webhook.WebhookClient.withUrl(webHookUrl).onThread(threadId)
                    .edit(messageDiscordId, newContent).get();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof HttpException httpEx) {
                if (httpEx.getMessage() != null && httpEx.getMessage().contains("10008")) {
                    System.out.println("해당 Discord 메시지가 존재하지 않음 (이미 삭제됐을 가능성)");
                    throw new CustomException(NOT_PROVIDE_CROSS_EDIT);
                } else {
                    throw httpEx; // 그대로 다시 던지거나 처리
                }
            }  else {
                log.error("thread Message Edit error (wrapped): " + cause.getMessage());
                throw new RuntimeException(cause); // 혹은 다시 감싸서 던지기
            }
        }catch(Exception e){
            log.error("thread Message Edit error:"+e.getMessage());
        }
    }
}
