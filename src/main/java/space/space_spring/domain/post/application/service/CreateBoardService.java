package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.Tag.CreateTagUseCase;
import space.space_spring.domain.post.application.port.in.boardCache.AddBoardCacheUseCase;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardCommand;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardUseCase;
import space.space_spring.domain.post.application.port.out.CreateBoardPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Tag;
import space.space_spring.global.exception.CustomException;

import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.BOARD_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CreateBoardService implements CreateBoardUseCase {

    private final CreateBoardPort createBoardPort;
    private final AddBoardCacheUseCase addBoardCacheUseCase;
    private final CreateTagUseCase createTagUseCase;
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

        //Tag 추가
        if(!command.getTags().isEmpty()) {
            List<Tag> tags= createTagUseCase.create(command.getTags(), boardId);
            log.info(String.join("\n",tags.stream().map(Tag::getTagName).toList()));
        }

        return boardId;
    }
}
