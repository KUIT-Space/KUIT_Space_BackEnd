package space.space_spring.domain.post.adapter.out.persistence.comment;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.like.QLikeJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.post.QPostJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.QPostBaseJpaEntity;
import space.space_spring.domain.post.application.port.out.comment.AnonymousCommentCreatorView;
import space.space_spring.domain.post.application.port.out.comment.CommentCreatorQueryPort;
import space.space_spring.domain.post.application.port.out.comment.CommentDetailQueryPort;
import space.space_spring.domain.post.application.port.out.comment.CommentDetailView;
import space.space_spring.domain.spaceMember.domian.QSpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentQueryAdapter implements CommentDetailQueryPort, CommentCreatorQueryPort {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentDetailView> loadCommentDetail(Long postId, Long spaceMemberId) {
        QPostCommentJpaEntity comment = QPostCommentJpaEntity.postCommentJpaEntity;
        QSpaceMemberJpaEntity commentCreator = QSpaceMemberJpaEntity.spaceMemberJpaEntity;
        QPostJpaEntity parentPost = QPostJpaEntity.postJpaEntity;
        QLikeJpaEntity commentLike = QLikeJpaEntity.likeJpaEntity;
        QPostBaseJpaEntity commentBase = QPostBaseJpaEntity.postBaseJpaEntity;

        return queryFactory.select(
                        Projections.constructor(CommentDetailView.class,
                                // 댓글 작성자 id
                                commentBase.spaceMember.id,
                                // 댓글 작성자 이름
                                commentBase.spaceMember.nickname,
                                // 댓글 작성자 프로필 이미지 URL
                                commentBase.spaceMember.profileImageUrl,
                                // 게시글 작성자와 댓글 작성자 비교
                                Expressions.booleanTemplate(
                                        "({0} = {1})",
                                        commentBase.spaceMember.id, parentPost.postBase.spaceMember.id),
                                // 댓글 내용
                                commentBase.content,
                                // 댓글 생성일 및 수정일
                                commentBase.createdAt,
                                commentBase.lastModifiedAt,
                                // 댓글 좋아요 개수 -> 서브쿼리로 집계
                                JPAExpressions.select(commentLike.id.count())
                                        .from(commentLike)
                                        .where(commentLike.postBase.id.eq(comment.id)
                                                .and(commentLike.isLiked.eq(true))
                                                .and(commentLike.status.eq(BaseStatusType.ACTIVE))),
                                // 스페이스 멤버가 해당 댓글을 좋아하는지
                                JPAExpressions.selectOne()
                                        .from(commentLike)
                                        .where(commentLike.postBase.id.eq(comment.id)
                                                .and(commentLike.spaceMember.id.eq(spaceMemberId))
                                                .and(commentLike.isLiked.eq(true))
                                                .and(commentLike.status.eq(BaseStatusType.ACTIVE)))
                                        .exists(),
                                // 댓글의 상태 -> 삭제된 댓글인지 아닌지
                                Expressions.booleanTemplate(
                                        "CASE WHEN {0} = {1} THEN true ELSE false END",
                                        commentBase.status, Expressions.constant(BaseStatusType.ACTIVE)),
                                // 익명 댓글 여부
                                comment.isAnonymous
                        )
                )
                .from(comment)
                .join(comment.postBase, commentBase)
                .join(comment.post, parentPost)
                .join(commentBase.spaceMember, commentCreator)
                .where(parentPost.id.eq(postId))
                .orderBy(commentBase.createdAt.asc())
                .fetch();
    }

    /**
     * 특정 게시글의 익명 댓글 작성자의 정보를 댓글 생성 시각 기준 오름차순으로 정렬하여 반환
     * -> 이때 삭제된 익명 댓글도 모두 포함 (익명 스페이서 1이 댓글을 삭제하더라도, 그다음은 익명 스페이서 2 여야한다)
     */
    @Override
    public List<AnonymousCommentCreatorView> loadAnonymousCommentCreators(Long postId) {
        QPostCommentJpaEntity comment = QPostCommentJpaEntity.postCommentJpaEntity;
        QPostJpaEntity parentPost = QPostJpaEntity.postJpaEntity;
        QPostBaseJpaEntity commentBase = QPostBaseJpaEntity.postBaseJpaEntity;

        return queryFactory.select(
                        Projections.constructor(AnonymousCommentCreatorView.class,
                                // 댓글 작성자 id
                                commentBase.spaceMember.id,
                                // 게시글 작성자와 댓글 작성자 비교
                                Expressions.booleanTemplate(
                                        "({0} = {1})",
                                        commentBase.spaceMember.id,
                                        parentPost.postBase.spaceMember.id),
                                // 댓글의 상태 -> 삭제된 댓글인지 아닌지
                                Expressions.booleanTemplate(
                                        "CASE WHEN {0} = {1} THEN true ELSE false END",
                                        commentBase.status, Expressions.constant(BaseStatusType.ACTIVE))
                        )
                )
                .from(comment)
                .join(comment.postBase, commentBase)
                .join(comment.post, parentPost)
                .where(
                        parentPost.id.eq(postId)
                                .and(comment.isAnonymous.eq(true))
                )
                .orderBy(commentBase.createdAt.asc())
                .fetch();
    }
}
