package space.space_spring.domain.post.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.post.application.port.in.CreateBoard.CreateBoardCommand;
import space.space_spring.domain.post.application.port.in.CreateBoard.CreateBoardUseCase;
import space.space_spring.domain.post.application.port.out.CreateBoardPort;
import space.space_spring.domain.post.domain.Board;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CreateBoardService implements CreateBoardUseCase {

    private final CreateBoardPort createBoardPort;

    @Override
    @Transactional
    public Long createBoard(CreateBoardCommand command) {
        // command -> Board 도메인 엔티티 생성
        Board board = Board.withoutId(command.getSpaceId(), command.getDiscordId(), command.getBoardName(), command.getBoardType(), command.getWebhookUrl());

        // Board 저장 및 ID 반환
        return createBoardPort.createBoard(board);
    }
}
