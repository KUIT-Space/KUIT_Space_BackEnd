package space.space_spring.domain.discord.adapter.out;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.CreateDiscordForumMessageCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordMessagePort;
import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadPort;
import space.space_spring.global.exception.CustomException;

import java.util.concurrent.CompletableFuture;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Component
@RequiredArgsConstructor
public class CreateDiscordThreadAdapter implements CreateDiscordThreadPort {
    private final JDA jda;
    private final CreateDiscordMessagePort createDiscordMessagePort;
    @Override
    @Async
    public CompletableFuture<Long> create(CreateDiscordThreadCommand command){
        if(isTextChannel(command.getGuildDiscordId(),command.getChannelDiscordId())){
            return createThreadInTextChannel(command);
        }else{
            return createThreadInForumChannel(command);
        }


    }

    @Async
    private CompletableFuture<Long> createThreadInForumChannel(CreateDiscordThreadCommand command){


        return createDiscordMessagePort.sendForum(mapToForumMessage(command));
    }


    @Async
    private CompletableFuture<Long> createThreadInTextChannel(CreateDiscordThreadCommand command){


        return createDiscordMessagePort.send(command.getStartMessage())
                .thenCompose(startId->{
                    return createThread(startId, command.getChannelDiscordId(), command.getThreadName());
                }).thenCompose(threadId->{
                    return createThreadMessage(threadId,command);
                });
    }
    @Async
    private CompletableFuture<Long> createThread(Long startMessageId, Long channelDiscordId, String threadName){
        return CompletableFuture.supplyAsync(()-> {
            return jda.getTextChannelById(channelDiscordId)
                    .createThreadChannel(threadName, startMessageId).complete().getIdLong();
        }).exceptionally(err-> {
            throw new CustomException(DISCORD_THREAD_CREATE_FAIL,err.getMessage());
        });
    }

    @Async
    private CompletableFuture<Long> createThreadMessage(Long threadId,CreateDiscordThreadCommand command){
        WebhookClient webhookClient = WebhookClient.withUrl(command.getWebHookUrl());

        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder()
                .setAvatarUrl(command.getAvatarUrl())
                .setUsername(command.getUserName())
                //.setThreadName(command.getThreadName())
                .setContent(command.getContentMessage());

        return  webhookClient.onThread(threadId).send( messageBuilder.build()).exceptionally(err-> {
                    throw new CustomException(DISCORD_THREAD_CREATE_FAIL);
                })
                .thenApply(msg->msg.getId());
    }

    @Async
    private boolean isTextChannel(Long guildId, Long channelId){
        Guild guild =jda.getGuildById(guildId);
        if(guild==null){
            throw new CustomException(DISCORD_GUILD_NOT_FOUND);
        }
        GuildChannel channel = guild.getChannelCache().getElementById(channelId);
        if(channel==null){
            throw new CustomException(DISCORD_CHANNEL_NOT_FOUND);
        }
        if(channel.getType()== ChannelType.TEXT){
            return true;
        }
        if(channel.getType()== ChannelType.FORUM){
            return false;
        }
        throw new CustomException(DISCORD_CHANNEL_TYPE_WRONG);

    }

    private CreateDiscordForumMessageCommand mapToForumMessage(CreateDiscordThreadCommand command){
        return CreateDiscordForumMessageCommand.builder()
                .title(command.getThreadName())
                .userName(command.getUserName())
                .content(command.getContentMessage())
                .guildDiscordId(command.getGuildDiscordId())
                .channelDiscordId(command.getChannelDiscordId())
                .webHookUrl(command.getWebHookUrl())
                .avatarUrl(command.getAvatarUrl())
                .build();
    }
}
