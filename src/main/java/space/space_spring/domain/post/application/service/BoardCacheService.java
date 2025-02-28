package space.space_spring.domain.post.application.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardCacheAdapter;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardPersistenceAdapter;
import space.space_spring.domain.post.domain.Board;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardCacheService {
    private final BoardCacheAdapter boardCacheAdapter;
    private final BoardPersistenceAdapter boardPersistenceAdapter;
    @PostConstruct
    public void loadBoardDataIntoRedis() {
        log.info("Loading Board data into Redis...");
        List<Board> boardList = boardPersistenceAdapter.findAll();
        if(boardList.isEmpty()){
            return;
        }
        boardList.forEach(board -> {
            Long discordId = board.getDiscordId();
            Long id = board.getId();
            boardCacheAdapter.create(discordId,id);
        });

        log.info("Board data loaded into Redis successfully.");
    }


}
