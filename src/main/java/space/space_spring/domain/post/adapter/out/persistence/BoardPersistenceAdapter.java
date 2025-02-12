package space.space_spring.domain.post.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.application.port.out.CreateBoardPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.space.adapter.out.persistence.SpringDataSpace;
import space.space_spring.domain.space.domain.SpaceJpaEntity;
import space.space_spring.global.exception.CustomException;

import java.util.Optional;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class BoardPersistenceAdapter implements CreateBoardPort {

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
}
