package space.space_spring.domain.post.adapter.out.persistence.post.custom;

import static space.space_spring.domain.post.adapter.out.persistence.post.QPostJpaEntity.postJpaEntity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import space.space_spring.domain.post.adapter.out.persistence.post.PostJpaEntity;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostJpaEntity> findLatestByBoardIds(List<Long> boardIds, int size) {
        return jpaQueryFactory.selectFrom(postJpaEntity)
                .where(postJpaEntity.postBase.board.id.in(boardIds))
                .orderBy(postJpaEntity.postBase.createdAt.desc())
                .limit(size)
                .fetch();
    }
}
