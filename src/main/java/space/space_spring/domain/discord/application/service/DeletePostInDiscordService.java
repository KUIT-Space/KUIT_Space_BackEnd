package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.deletePost.DeletePostInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.deletePost.DeletePostInDiscordUseCase;

@Service
@RequiredArgsConstructor
public class DeletePostInDiscordService implements DeletePostInDiscordUseCase {

    @Override
    public void deletePost(DeletePostInDiscordCommand command) {
        // 상준님 구현해주세요!

    }
}
