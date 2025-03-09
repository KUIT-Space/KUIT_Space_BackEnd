package space.space_spring.domain.post.application.port.in.boardCache;

import java.util.List;
import java.util.Optional;

public interface LoadBoardCacheUseCase {
    //cache 에서 discordId로 board 를 찾고, boardID를 반환
    Optional<Long> findByDiscordId(Long discordId);

    List<Long> findAllChannel();
}
