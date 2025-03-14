package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.updatePost.UpdatePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.updatePost.UpdatePostInDiscordUseCase;
import space.space_spring.domain.discord.application.port.out.updateMessage.UpdateMessageInDiscordPort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UpdatePostInDiscordService implements UpdatePostInDiscordUseCase {

    private final UpdateMessageInDiscordPort updateMessageInDiscordPort;
    @Override
    public void updatePostInDiscord(UpdatePostInDiscordCommand command) {
        // 상준님 구현해주세요!!
        updateMessageInDiscordPort.editMessage(command.getWebHookUrl(), command.getDiscordIdOfBoard()
                ,command.getDiscordIdOfPost(), command.getTitle() ,command.getContent().getValue(), command.getDiscordIdOfTags());
    }
}
