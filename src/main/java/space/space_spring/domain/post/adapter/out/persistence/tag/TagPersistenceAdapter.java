package space.space_spring.domain.post.adapter.out.persistence.tag;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.BOARD_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.TAG_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.board.SpringDataBoardRepository;
import space.space_spring.domain.post.application.port.out.LoadTagPort;
import space.space_spring.domain.post.domain.Board;
import space.space_spring.domain.post.domain.Tag;
import space.space_spring.global.exception.CustomException;

@Repository
@RequiredArgsConstructor
public class TagPersistenceAdapter implements LoadTagPort {

    private final SpringDataBoardRepository boardRepository;
    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    @Override
    public Tag loadByBoardAndName(Board board, String name) {
        BoardJpaEntity boardJpaEntity = boardRepository.findById(board.getId())
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        return tagRepository.findByBoardIdAndTagName(boardJpaEntity, name).map(tagMapper::toDomainEntity)
                .orElseThrow(() -> new CustomException(TAG_NOT_FOUND));
    }
}
