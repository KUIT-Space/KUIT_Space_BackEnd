package space.space_spring.domain.post.application.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardCacheAdapter;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardPersistenceAdapter;
import space.space_spring.domain.post.application.port.in.loadBoard.LoadBoardUseCase;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.application.port.in.AddBoardCacheUseCase;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardCacheService implements AddBoardCacheUseCase {
    private final BoardCacheAdapter boardCacheAdapter;
    private final BoardPersistenceAdapter boardPersistenceAdapter;
    private final LoadBoardUseCase loadBoardUseCase;
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

    @Override
    public boolean add(Long boardId){
        Board board = loadBoardUseCase.findById(boardId);
        if(board==null){
            return false;
        }
        boardCacheAdapter.create(board.getDiscordId(),board.getId());
        return true;
    }



}
