package space.space_spring.domain.post.adapter.out.persistence.post.custom;

import static space.space_spring.domain.post.adapter.out.persistence.board.QBoardJpaEntity.boardJpaEntity;
import static space.space_spring.domain.post.adapter.out.persistence.post.QPostJpaEntity.postJpaEntity;
import static space.space_spring.domain.post.adapter.out.persistence.postBase.QPostBaseJpaEntity.postBaseJpaEntity;
import static space.space_spring.domain.post.adapter.out.persistence.postTag.QPostTagJpaEntity.postTagJpaEntity;
import static space.space_spring.domain.post.adapter.out.persistence.tag.QTagJpaEntity.tagJpaEntity;
import static space.space_spring.global.common.enumStatus.BaseStatusType.ACTIVE;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.post.PostJpaEntity;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostJpaEntity> findLatestByBoardIds(List<Long> boardIds, int size) {
        return jpaQueryFactory.selectFrom(postJpaEntity)
                .where(
                        postJpaEntity.postBase.board.id.in(boardIds),
                        postJpaEntity.postBase.status.eq(ACTIVE)
                )
                .orderBy(postJpaEntity.postBase.createdAt.desc())
                .limit(size)
                .fetch();
    }

    @Override
    public Optional<PostJpaEntity> findLatestByBoardIdAndTagId(Long boardId, Long tagId) {
        return Optional.ofNullable(jpaQueryFactory.selectFrom(postJpaEntity)
                .join(postJpaEntity.postBase, postBaseJpaEntity)
                .join(postBaseJpaEntity.board, boardJpaEntity)
                .join(postTagJpaEntity).on(postTagJpaEntity.postBase.eq(postBaseJpaEntity))
                .join(tagJpaEntity).on(postTagJpaEntity.tag.eq(tagJpaEntity))
                .where(
                        boardJpaEntity.id.eq(boardId),
                        tagJpaEntity.id.eq(tagId),
                        postJpaEntity.postBase.status.eq(ACTIVE)
                )
                .orderBy(postJpaEntity.postBase.createdAt.desc())
                .fetchFirst());
    }
}
