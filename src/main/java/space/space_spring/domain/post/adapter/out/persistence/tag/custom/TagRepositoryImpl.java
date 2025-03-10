package space.space_spring.domain.post.adapter.out.persistence.tag.custom;

import static space.space_spring.domain.post.adapter.out.persistence.tag.QTagJpaEntity.tagJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.tag.TagJpaEntity;

@RequiredArgsConstructor
public class TagRepositoryImpl implements TagRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<TagJpaEntity> findByBoardIdAndTagName(BoardJpaEntity board, String tagName) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(tagJpaEntity)
                        .where(tagJpaEntity.board.eq(board)
                                .and(tagJpaEntity.tagName.eq(tagName))
                        )
                        .fetchOne()
        );
    }
}
