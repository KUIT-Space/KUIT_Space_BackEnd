package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.deleteBoard.DeleteBoardUseCase;
import space.space_spring.domain.post.application.port.in.loadBoard.LoadBoardUseCase;
import space.space_spring.domain.post.application.port.out.DeleteBoardCachePort;
import space.space_spring.domain.post.application.port.out.DeleteBoardPort;

@Service
@RequiredArgsConstructor
public class DeleteBoardService implements DeleteBoardUseCase {
    private final DeleteBoardCachePort deleteBoardCachePort;
    private final DeleteBoardPort deleteBoardPort;
    private final LoadBoardUseCase loadBoardUseCase;
    @Override
    @Transactional
    public boolean delete(Long boardId){

        Long discordId = loadBoardUseCase.findById(boardId).getDiscordId();
        deleteBoardPort.delete(boardId);
        deleteBoardCachePort.deleteByDiscordId(discordId);
        return true;
    }

}
