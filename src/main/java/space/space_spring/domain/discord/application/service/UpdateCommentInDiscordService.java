package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.updateComment.UpdateCommentInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.updateComment.UpdateCommentInDiscordUseCase;
import space.space_spring.domain.discord.application.port.out.updateMessage.UpdateMessageInDiscordPort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdateCommentInDiscordService implements UpdateCommentInDiscordUseCase {
    private final UpdateMessageInDiscordPort updateMessageInDiscordPort;
    @Override
    public void updateCommentInDiscord(UpdateCommentInDiscordCommand command) {
        // 상준님 구현해주세요!
        updateMessageInDiscordPort.editMessage(command.getWebHookUrl(), command.getDiscordIdOfBoard()
                ,command.getDiscordIdOfPost(), command.getNewContent(), List.of());

    }
}
