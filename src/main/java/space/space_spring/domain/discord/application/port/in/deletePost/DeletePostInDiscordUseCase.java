package space.space_spring.domain.discord.application.port.in.deletePost;

import java.util.List;

public interface DeletePostInDiscordUseCase {

    void deletePost(DeletePostInDiscordCommand command);
}
