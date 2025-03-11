package space.space_spring.domain.post.application.port.out;

import java.util.List;
import java.util.Optional;

public interface LoadBoardCachePort {
    Optional<Long> findByDiscordId(Long discordId);
    List<Long> findAllChannel();
}
