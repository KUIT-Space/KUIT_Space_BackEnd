package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.post.application.port.in.loadBoard.LoadBoardUseCase;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.BoardType;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoadBoardService implements LoadBoardUseCase {
    private final LoadBoardPort loadBoardPort;
    @Override
    public Board findById(Long boardId){
        return loadBoardPort.loadById(boardId);
    }
    @Override
    public Optional<BoardType> getBoardTypeById(Long boardId){
        Board board = findById(boardId);
        if(board==null){
            return Optional.empty();
        }
        return Optional.of(board.getBoardType());
    }

    @Override
    public Optional<Board> findByDiscordId(Long discordId){
        return loadBoardPort.loadByDiscordId(discordId);
    }

}
