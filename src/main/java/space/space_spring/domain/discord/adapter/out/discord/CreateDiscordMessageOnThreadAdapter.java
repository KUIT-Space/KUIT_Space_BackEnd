package space.space_spring.domain.discord.adapter.out.discord;

import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.WebhookClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.CheckDiscordChannelPort;
import space.space_spring.domain.discord.application.port.out.CreateDiscordMessageOnThreadCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordMessageOnThreadPort;
import space.space_spring.global.exception.CustomException;

import java.util.concurrent.CompletableFuture;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.DISCORD_THREAD_CREATE_FAIL;

@Component
@RequiredArgsConstructor
public class CreateDiscordMessageOnThreadAdapter implements CreateDiscordMessageOnThreadPort {

    private final CheckDiscordChannelPort checkDiscordChannelPort;
    private final DiscordThreadRepository discordThreadRepository;

    @Override
    public CompletableFuture<Long> sendToThread(CreateDiscordMessageOnThreadCommand command){
        //만약 아직 해당 message에 thread가 생성되지 않았다면, thread를 생성합니다.
        if(!checkDiscordChannelPort.isThreadExist(command.getGuildDiscordId(), command.getThreadChannelDiscordId())){
            return discordThreadRepository.createThread(command.getThreadChannelDiscordId(),command.getOriginChannelId(),"temp")
                    //ToDo Load Post Title name: "temp"->name
                    .thenCompose(threadId->{
                        return discordThreadRepository.createThreadMessage(threadId, command.getWebHookMessage());
                    });
        }

        return discordThreadRepository.createThreadMessage(command.getThreadChannelDiscordId(), command.getWebHookMessage());

    }


}
