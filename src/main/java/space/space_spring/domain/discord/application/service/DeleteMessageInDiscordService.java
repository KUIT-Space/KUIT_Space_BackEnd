package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.deleteComment.DeleteCommentInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.deleteComment.DeleteCommentInDiscordUseCase;
import space.space_spring.domain.discord.application.port.in.deletePost.DeletePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.deletePost.DeletePostInDiscordUseCase;
import space.space_spring.domain.discord.application.port.out.deleteWebHookMessage.DeleteDiscordWebHookMessagePort;

@Service
@RequiredArgsConstructor
public class DeleteMessageInDiscordService implements DeleteCommentInDiscordUseCase , DeletePostInDiscordUseCase {
    private final DeleteDiscordWebHookMessagePort deleteDiscordWebHookMessagePort;
    @Override
    public void deleteCommentInDiscord(DeleteCommentInDiscordCommand command) {
        deleteDiscordWebHookMessagePort.deleteInThread(command.getWebHookUrl(), command.getDiscordIdOfSpace(),
                command.getDiscordIdOfPost(), command.getDiscordIdOfComment());
    }
    @Override
    public void deletePostInDiscord(DeletePostInDiscordCommand command){
        deleteDiscordWebHookMessagePort.delete(command.getWebHookUrl(), command.getDiscordIdOfSpace(),
                command.getDiscordIdOfBoard(), command.getDiscordIdOfPost());
    }
}
