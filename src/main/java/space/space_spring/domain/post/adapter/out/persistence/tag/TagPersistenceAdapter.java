package space.space_spring.domain.post.adapter.out.persistence.tag;

import static space.space_spring.global.common.enumStatus.BaseStatusType.ACTIVE;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.TAG_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.board.SpringDataBoardRepository;
import space.space_spring.domain.post.application.port.out.LoadTagPort;
import space.space_spring.domain.post.domain.Tag;
import space.space_spring.global.exception.CustomException;

@Repository
@RequiredArgsConstructor
public class TagPersistenceAdapter implements LoadTagPort {

    private final SpringDataBoardRepository boardRepository;
    private final SpringDataTagRepository springDataTagRepository;
    private final TagMapper tagMapper;

    @Override
    public Tag loadByIdAndBoard(Long tagId, Long boardId) {
        return springDataTagRepository.findByIdAndBoardIdAndStatus(tagId, boardId, ACTIVE).map(tagMapper::toDomainEntity)
                .orElseThrow(() -> new CustomException(TAG_NOT_FOUND));
    }
}
