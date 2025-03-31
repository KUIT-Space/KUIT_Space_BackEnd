package space.space_spring.domain.discord.adapter.out.discord;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.WebhookClient;
import net.dv8tion.jda.api.entities.channel.forums.ForumTag;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.discord.application.port.out.CreateDiscordWebHookMessageCommand;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
@RequiredArgsConstructor
public class DiscordForumRepository {
    private final JDA jda;

    @Async
    public CompletableFuture<Long> sendForum(CreateDiscordWebHookMessageCommand command){
        WebhookClient client= WebhookClient.createClient(jda,command.getWebHookUrl());

        CompletableFuture<Long> future = new CompletableFuture<>();

        client.sendMessage(command.getMessageContent())
                .setAvatarUrl(command.getAvatarUrl())
                .createThread(command.getTitle())
                .setUsername(command.getName()).queue(
                        obj->{
                            if(obj instanceof Message message) {
                                List<ForumTag> allTags = jda.getForumChannelById(command.getChannelDiscordId()).getAvailableTags();
                                message.getChannel().asThreadChannel().getManager().setAppliedTags(
                                        allTags.stream().filter(tag->command.getTags().contains(tag.getIdLong())).toList()
                                ).queue();
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
