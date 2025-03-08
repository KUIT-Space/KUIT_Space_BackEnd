package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.boardCache.AddBoardCacheUseCase;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardCommand;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardUseCase;
import space.space_spring.domain.post.application.port.out.CreateBoardPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.BOARD_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateBoardService implements CreateBoardUseCase {

    private final CreateBoardPort createBoardPort;
    private final AddBoardCacheUseCase addBoardCacheUseCase;
    @Override
    @Transactional
    public Long createBoard(CreateBoardCommand command) {
        // command -> Board 도메인 엔티티 생성
        Board board = Board.withoutId(command.getSpaceId(), command.getDiscordId(), command.getBoardName(), command.getBoardType(), command.getWebhookUrl());
        // Board 저장 및 ID 반환
        Long boardId = createBoardPort.createBoard(board);
        if(!addBoardCacheUseCase.add(boardId)){
            throw new CustomException(BOARD_NOT_FOUND, BOARD_NOT_FOUND.getMessage()+": redis 저장과정에서 찾을 수 없습니다");
        }

        return boardId;
    }
}
