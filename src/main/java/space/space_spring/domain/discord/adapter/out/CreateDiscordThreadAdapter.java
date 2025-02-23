package space.space_spring.domain.discord.adapter.out;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.CreateDiscordMessagePort;
import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadPort;
import space.space_spring.global.exception.CustomException;

import java.util.concurrent.CompletableFuture;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.DISCORD_THREAD_CREATE_FAIL;

@Component
@RequiredArgsConstructor
public class CreateDiscordThreadAdapter implements CreateDiscordThreadPort {
    private final JDA jda;
    private final CreateDiscordMessagePort createDiscordMessagePort;
    @Override
    public CompletableFuture<Long> create(CreateDiscordThreadCommand command){
        WebhookClient webhookClient = WebhookClient.withUrl(command.getWebHookUrl());

        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder()
                .setAvatarUrl(command.getAvatarUrl())
                .setUsername(command.getUserName())
                .setContent(command.getContentMessage());

        return createDiscordMessagePort.send(command.getStartMessage())
                .thenCompose(startId->{
                    return createThread(startId, command.getChannelDiscordId(), command.getThreadName());
                }).thenCompose(threadId->{
                    return createThreadMessage(threadId,webhookClient,messageBuilder.build());
                });
    }

    private CompletableFuture<Long> createThread(Long startMessageId, Long channelDiscordId, String threadName){
        return CompletableFuture.supplyAsync(()-> {
            return jda.getTextChannelById(channelDiscordId)
                    .createThreadChannel(threadName, startMessageId).complete().getIdLong();
        }).exceptionally(err-> {
            throw new CustomException(DISCORD_THREAD_CREATE_FAIL);
        });
    }

    private CompletableFuture<Long> createThreadMessage(Long threadId,WebhookClient webhookClient ,WebhookMessage message){

        return  webhookClient.onThread(threadId).send(message).exceptionally(err-> {
                    throw new CustomException(DISCORD_THREAD_CREATE_FAIL);
                })
                .thenApply(msg->msg.getId());
    }
}
