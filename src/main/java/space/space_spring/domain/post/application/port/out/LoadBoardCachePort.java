package space.space_spring.domain.post.application.port.out;

import java.util.Optional;

public interface LoadBoardCachePort {
    Optional<String> load(Long discordId);
}
