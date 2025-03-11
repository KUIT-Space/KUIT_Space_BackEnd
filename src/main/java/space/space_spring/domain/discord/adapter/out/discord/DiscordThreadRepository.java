package space.space_spring.domain.discord.adapter.out.discord;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordWebHookMessageCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordWebHookMessagePort;
import space.space_spring.global.exception.CustomException;

import java.util.concurrent.CompletableFuture;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.DISCORD_CHANNEL_TYPE_WRONG;

@Repository
@RequiredArgsConstructor
public class DiscordThreadRepository {
    private final JDA jda;
    private final CreateDiscordWebHookMessagePort createDiscordWebHookMessagePort;
    private final DiscordForumRepository discordForumRepository;

    @Async
    public CompletableFuture<Long> create(CreateDiscordThreadCommand command){
        //channel 종류에 따라 thread 생성 방식이 달라집니다
        if(isTextChannel(command.getGuildDiscordId(),command.getChannelDiscordId())){
            return createThreadInTextChannel(command);
        }else{
            return createThreadInForumChannel(command);
        }


    }



    @Async
    public CompletableFuture<Long> createThreadInForumChannel(CreateDiscordThreadCommand command){

        //Forum channel 에서는 한번에 thread 생성가능
        return discordForumRepository.sendForum(mapToWebHookMessage(command));
    }


    @Async
    public CompletableFuture<Long> createThreadInTextChannel(CreateDiscordThreadCommand command){

        /*Text channel 에서 webhook 으로 thread 를 생성하기 위해서는 다음 과정을 거칩니다
         * 1. channel 에 webhook message 작성 : send()
         * 2. 해당 message 를 시작으로 thread channel 생성 : createThread()
         * 3. 생성된 thread channel 에 첫 message 작성 : createThreadMessage()
         *
         * 3에서 OnThread()를 webhook 을 대상으로 사용하기 위해 minnced 패키지를 사용합니다.
         * 1-3과정을 비동기로 연결 : CompletableFuture.thenCompose()로 각 앞 과정이 완료되었음을 보장하면서 연결
         */
        return createDiscordWebHookMessagePort.send(command.getStartMessage())
                .thenCompose(startId->{
                    return createThread(startId, command.getChannelDiscordId(), command.getThreadName());
                }).thenCompose(threadId->{
                    return createThreadMessage(threadId,command.getContentWebHookMessage());
                });
    }

    @Async
    public CompletableFuture<Long> createThread(Long startMessageId, Long channelDiscordId, String threadName){
        return CompletableFuture.supplyAsync(()-> {
            return jda.getTextChannelById(channelDiscordId)
                    .createThreadChannel(threadName, startMessageId).complete().getIdLong();
        }).exceptionally(err-> {
            throw new CustomException(DISCORD_THREAD_CREATE_FAIL,err.getMessage());
        });
    }

    @Async
    public CompletableFuture<Long> createThreadMessage(Long threadId,WebHookMessage message){
        WebhookClient webhookClient = WebhookClient.withUrl(message.getWebHookUrl());

        WebhookMessageBuilder messageBuilder = new WebhookMessageBuilder()
                .setAvatarUrl(message.getAvatarUrl())
                .setUsername(message.getUserName())
                //.setThreadName(command.getThreadName())
                .setContent(message.getContent());

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

    private CreateDiscordWebHookMessageCommand mapToWebHookMessage(CreateDiscordThreadCommand command){
        return CreateDiscordWebHookMessageCommand.builder()
                .title(command.getThreadName())
                .name(command.getUserName())
                .content(command.getContentMessage())
                .guildDiscordId(command.getGuildDiscordId())
                .channelDiscordId(command.getChannelDiscordId())
                .webHookUrl(command.getWebHookUrl())
                .avatarUrl(command.getAvatarUrl())
                .build();
    }
}
