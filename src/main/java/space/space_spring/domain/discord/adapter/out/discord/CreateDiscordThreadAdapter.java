package space.space_spring.domain.discord.adapter.out.discord;

import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadCommand;
import space.space_spring.domain.discord.application.port.out.CreateDiscordThreadPort;
import space.space_spring.domain.discord.application.port.out.CreateDiscordWebHookMessageCommand;
import space.space_spring.global.exception.CustomException;

import java.util.concurrent.CompletableFuture;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.DISCORD_CHANNEL_TYPE_WRONG;

@Component
@RequiredArgsConstructor
public class CreateDiscordThreadAdapter implements CreateDiscordThreadPort {
    private final DiscordForumRepository discordForumRepository;
    private final DiscordThreadRepository discordThreadRepository;
    private final DiscordChannelAdapter discordChannelAdapter;
    @Override
    public CompletableFuture<Long> create(CreateDiscordThreadCommand command){
        if(discordChannelAdapter.isTextChannel(command.getChannelDiscordId())){
            return discordThreadRepository.createThreadInTextChannel(command);
        }

        if(discordChannelAdapter.isForumChannel(command.getChannelDiscordId())){
            return discordForumRepository.sendForum(mapToWebHookCommand(command));
        }
        throw new CustomException(DISCORD_CHANNEL_TYPE_WRONG);
    }

    private CreateDiscordWebHookMessageCommand mapToWebHookCommand(CreateDiscordThreadCommand command){
        return CreateDiscordWebHookMessageCommand.builder()
                .webHookUrl(command.getWebHookUrl())
                .channelDiscordId(command.getChannelDiscordId())
                .guildDiscordId(command.getGuildDiscordId())
                .name(command.getUserName())
                .content(command.getContentMessage())
                .title(command.getThreadName())
                .avatarUrl(command.getAvatarUrl())
                .build();
    }
}
