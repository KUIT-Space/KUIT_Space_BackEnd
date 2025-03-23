package space.space_spring.domain.post.adapter.out.persistence.post;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.attachment.QAttachmentJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.like.QLikeJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.QPostBaseJpaEntity;
import space.space_spring.domain.post.application.port.out.post.PostDetailQueryPort;
import space.space_spring.domain.post.application.port.out.post.PostDetailView;
import space.space_spring.domain.spaceMember.domian.QSpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;
import space.space_spring.global.exception.CustomException;

import java.util.Map;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.POST_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class PostQueryAdapter implements PostDetailQueryPort {

    private static final String ANONYMOUS_POST_CREATOR_NICKNAME = "익명 스페이서";
    private final JPAQueryFactory queryFactory;

    @Override
    public PostDetailView loadPostDetail(Long postId) {
        QPostJpaEntity postJpaEntity = QPostJpaEntity.postJpaEntity;
        QPostBaseJpaEntity postBaseJpaEntity = QPostBaseJpaEntity.postBaseJpaEntity;
        QSpaceMemberJpaEntity spaceMemberJpaEntity = QSpaceMemberJpaEntity.spaceMemberJpaEntity;
        QAttachmentJpaEntity attachmentJpaEntity = QAttachmentJpaEntity.attachmentJpaEntity;
        QLikeJpaEntity likeJpaEntity = QLikeJpaEntity.likeJpaEntity;

        Map<Long, PostDetailView> resultMap = queryFactory
                .from(postJpaEntity)
                .leftJoin(postJpaEntity.postBase, postBaseJpaEntity)
                .leftJoin(postBaseJpaEntity.spaceMember, spaceMemberJpaEntity)
                .leftJoin(attachmentJpaEntity).on(attachmentJpaEntity.postBase.eq(postBaseJpaEntity))
                .where(postJpaEntity.id.eq(postId)
                        .and(postBaseJpaEntity.status.eq(BaseStatusType.ACTIVE)))
                .transform(GroupBy.groupBy(postJpaEntity.id).as(
                        Projections.constructor(PostDetailView.class,
                                // 게시글 작성자 이름: 익명 여부에 따라 결정
                                Expressions.stringTemplate("CASE WHEN {0} = true THEN {1} ELSE {2}",
                                        postJpaEntity.isAnonymous,
                                        Expressions.constant(ANONYMOUS_POST_CREATOR_NICKNAME),
                                        spaceMemberJpaEntity.nickname),
                                // 게시글 작성자 프로필 이미지 URL
                                spaceMemberJpaEntity.profileImageUrl,
                                // 게시글 생성 시각
                                postBaseJpaEntity.createdAt,
                                // 게시글 수정 시각
                                postBaseJpaEntity.lastModifiedAt,
                                // 제목
                                postJpaEntity.title,
                                // 내용
                                postBaseJpaEntity.content,
                                // 첨부파일 URL 리스트
                                GroupBy.list(attachmentJpaEntity.attachmentUrl),
                                // 좋아요 수 -> 서브쿼리로 집계
                                JPAExpressions.select(likeJpaEntity.id.count())
                                        .from(likeJpaEntity)
                                        .where(likeJpaEntity.postBase.id.eq(postBaseJpaEntity.id)
                                                .and(likeJpaEntity.isLiked.eq(true))
                                                .and(likeJpaEntity.status.eq(BaseStatusType.ACTIVE))),
                                // 게시글 작성자가 해당 게시글을 좋아하는지
                                JPAExpressions.selectOne()
                                        .from(likeJpaEntity)
                                        .where(likeJpaEntity.postBase.id.eq(postBaseJpaEntity.id)
                                                .and(likeJpaEntity.spaceMember.id.eq(postBaseJpaEntity.spaceMember.id))
                                                .and(likeJpaEntity.status.eq(BaseStatusType.ACTIVE)))
                                        .exists()
                        )
                ));

        PostDetailView result = resultMap.get(postId);
        if (result == null) {
            throw new CustomException(POST_NOT_FOUND);
        }

        return result;
    }
}
