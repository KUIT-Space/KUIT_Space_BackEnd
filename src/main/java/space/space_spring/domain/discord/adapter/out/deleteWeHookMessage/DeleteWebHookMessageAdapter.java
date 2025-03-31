package space.space_spring.domain.discord.adapter.out.deleteWeHookMessage;

import club.minnced.discord.webhook.exception.HttpException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.deleteWebHookMessage.DeleteDiscordWebHookMessagePort;
import space.space_spring.global.exception.CustomException;

import java.util.concurrent.ExecutionException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.NOT_PROVIDE_CROSS_DELETE;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.NOT_PROVIDE_CROSS_EDIT;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteWebHookMessageAdapter implements DeleteDiscordWebHookMessagePort {
    private final JDA jda;
    @Override
    public void delete(String webHook,Long guildDiscordId, Long channelDiscordId,Long messageId){
        try {
            WebhookClient.createClient(jda, webHook).deleteMessageById(messageId).complete();
        }catch(ErrorResponseException e){
            if(e.getErrorCode()==10008){
                throw new CustomException(NOT_PROVIDE_CROSS_DELETE);
            }
        }
    }

    public void deleteInThread(String webHook,Long guildDiscordId, Long threadDiscordId,Long messageId){
        try {
            club.minnced.discord.webhook.WebhookClient.withUrl(webHook)
                    .onThread(threadDiscordId)
                    .delete(messageId).get();
        }catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof HttpException httpEx) {
                if (httpEx.getMessage() != null && httpEx.getMessage().contains("10008")) {
                    System.out.println("해당 Discord 메시지가 존재하지 않음 (웹훅이 아닌 디스코드에서 작성 되었을 가능성)");
                    throw new CustomException(NOT_PROVIDE_CROSS_DELETE);
                } else {
                    throw httpEx; // 그대로 다시 던지거나 처리
                }
            }  else {
                log.error("thread Message Edit error (wrapped): " + cause.getMessage());
                throw new RuntimeException(cause); // 혹은 다시 감싸서 던지기
            }
        }catch(Exception e){
            log.error("thread Message Delete error:"+e.getMessage());
        }
    }

}
