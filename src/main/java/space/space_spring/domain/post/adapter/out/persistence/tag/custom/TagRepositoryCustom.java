package space.space_spring.domain.post.adapter.out.persistence.tag.custom;

import java.util.Optional;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.tag.TagJpaEntity;

public interface TagRepositoryCustom {
    Optional<TagJpaEntity> findByBoardIdAndTagName(BoardJpaEntity board, String tagName);
}
