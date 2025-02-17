package space.space_spring.domain.discord.adapter.out;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.CreateDiscordMessageCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordMessagePort;
import space.space_spring.global.exception.CustomException;

import java.util.concurrent.CompletableFuture;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SEND_MESSAGE_FAIL;

@Component
@RequiredArgsConstructor
public class CreateDiscordMessageAdapter implements CreateDiscordMessagePort {
    private final JDA jda;
    @Override
    public CompletableFuture<Long> send(CreateDiscordMessageCommand command){
        WebhookClient client= WebhookClient.createClient(jda,command.getWebHookUrl());

        CompletableFuture<Long> future = new CompletableFuture<>();

        client.sendMessage(command.getContent())
                .setAvatarUrl(command.getAvatarUrl())

                .setUsername(command.getName()).queue(
                    obj->{
                        if(obj instanceof Message) {
                            Message message = (Message) obj;
                            future.complete(message.getIdLong());
                        }else{
                            future.completeExceptionally(new RuntimeException("Webhook의 return 값이 message 타입이 아닙니다"));
                        }
                    }
                    ,throwable->{
                            future.completeExceptionally((Throwable)throwable);
                        }
                );

        return future;
//        return client.send(message).handle((result,throwable)->{
//            if(throwable!=null){
//                throw new CustomException(SEND_MESSAGE_FAIL, SEND_MESSAGE_FAIL.getMessage()+":"+throwable.toString());
//            }
//            return result;
//        }
//
//        ).thenApply(msg-> msg.getId());

    }

//    private WebhookClient getWebHookClient(String webHookUrl){
//        // Using the builder
//        WebhookClientBuilder builder = new WebhookClientBuilder(webHookUrl); // or id, token
//        builder.setThreadFactory((job) -> {
//            Thread thread = new Thread(job);
//            thread.setName("Hello");
//            thread.setDaemon(true);
//
//            return thread;
//        });
//        builder.setWait(true);
//        return builder.build();
//    }
//
//    private WebhookMessage mapToMessage(CreateDiscordMessageCommand command){
//        return new WebhookMessageBuilder()
//                .setAvatarUrl(command.getAvatarUrl())
//                .setUsername(command.getName())
//                .setContent(command.getContent())
//                .build();
//    }
}
