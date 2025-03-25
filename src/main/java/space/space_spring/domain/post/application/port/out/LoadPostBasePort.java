package space.space_spring.domain.post.application.port.out;

import java.util.Optional;

public interface LoadPostBasePort {
    Optional<Long> loadByDiscordId(Long discordId);
}
