package space.space_spring.domain.post.adapter.out.persistence.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.application.port.out.CreateBoardPort;
import space.space_spring.domain.post.application.port.out.LoadBoardPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.space.adapter.out.persistence.SpringDataSpace;
import space.space_spring.domain.space.domain.SpaceJpaEntity;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.BOARD_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class BoardPersistenceAdapter implements CreateBoardPort, LoadBoardPort {

    private final SpringDataBoardRepository boardRepository;
    private final SpringDataSpace spaceRepository;
    private final BoardMapper boardMapper;

    @Override
    public Long createBoard(Board board) {
        SpaceJpaEntity spaceJpaEntity = spaceRepository.findById(
                board.getSpaceId()).orElseThrow(() -> new CustomException(SPACE_NOT_FOUND));

        BoardJpaEntity boardJpaEntity = boardMapper.toJpaEntity(spaceJpaEntity, board);
        return boardRepository.save(boardJpaEntity).getId();
    }

    @Override
    public Board loadById(Long id) {
        BoardJpaEntity boardJpaEntity = boardRepository.findById(id)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        return boardMapper.toDomainEntity(boardJpaEntity);
    }
}
