package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.post.application.port.in.deleteBoard.DeleteBoardUseCase;
import space.space_spring.domain.post.application.port.in.loadBoard.LoadBoardUseCase;
import space.space_spring.domain.post.application.port.out.DeleteBoardCachePort;

@Service
@RequiredArgsConstructor
public class DeleteBoardService implements DeleteBoardUseCase {
    private final DeleteBoardCachePort deleteBoardCachePort;
    private final LoadBoardUseCase loadBoardUseCase;
    @Override
    public boolean delete(Long boardId){
        //Todo 구현 필요
        Long discordId = loadBoardUseCase.findById(boardId).getDiscordId();
        deleteBoardCachePort.deleteByDiscordId(discordId);
        return true;
    }

}
