package space.space_spring.domain.discord.adapter.out.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.WebhookClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.CheckDiscordChannelPort;
import space.space_spring.domain.discord.application.port.out.CreateDiscordWebHookMessageCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordWebHookMessagePort;
import space.space_spring.global.exception.CustomException;

import java.util.concurrent.CompletableFuture;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.DISCORD_CHANNEL_TYPE_WRONG;

@Component
@RequiredArgsConstructor
public class CreateDiscordWebHookMessageAdapter implements CreateDiscordWebHookMessagePort {
    private final JDA jda;
    private final CheckDiscordChannelPort checkDiscordChannelPort;
    private final DiscordForumRepository discordForumRepository;
    @Override
    @Async
    public CompletableFuture<Long> send(CreateDiscordWebHookMessageCommand command){
        if(checkDiscordChannelPort.isTextChannel(command.getChannelDiscordId())){
            return sendText(command);
        }
        if(checkDiscordChannelPort.isForumChannel(command.getChannelDiscordId())){
            return discordForumRepository.sendForum(command);
        }

        throw new CustomException(DISCORD_CHANNEL_TYPE_WRONG
                ,DISCORD_CHANNEL_TYPE_WRONG.getMessage()
                +":"+checkDiscordChannelPort.getChannelType(command.getGuildDiscordId(), command.getChannelDiscordId()));
    }

    private CompletableFuture<Long> sendText(CreateDiscordWebHookMessageCommand command){
        WebhookClient client= WebhookClient.createClient(jda,command.getWebHookUrl());

        CompletableFuture<Long> future = new CompletableFuture<>();

        client.sendMessage(command.getContent())
                .setAvatarUrl(command.getAvatarUrl())
                .setUsername(command.getName()).queue(
                        obj->{
                            if(obj instanceof Message message) {
                                client.sendMessage("msg ID:"+message.getId()).queue();
                                future.complete(message.getIdLong());
                            }else{
                                future.completeExceptionally(new RuntimeException("Webhook의 return 값이 message 타입이 아닙니다"));
                            }
                        }
                        ,throwable->{

                            System.out.println("\n\nerror"+throwable.toString());
                            future.completeExceptionally((Throwable)throwable);
                        }
                );

        return future;
    }


}
