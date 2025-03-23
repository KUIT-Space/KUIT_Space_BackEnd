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

import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.POST_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class PostQueryAdapter implements PostDetailQueryPort {

    private static final String ANONYMOUS_POST_CREATOR_NICKNAME = "익명 스페이서";
    private final JPAQueryFactory queryFactory;

    /**
     * 게시글 기본 정보 조회 쿼리 1번
     * 게시글의 첨부파일 조회 쿼리 1번
     * -> 총 2번의 쿼리로 구성
     * -> 한번의 쿼리로 갈 수 있었으나 hibernate 버전과 queryDsl의 버전이 맞지않아 에러가 발생해서 일단 쿼리 2번으로 분리하였음
     */
    @Override
    public PostDetailView loadPostDetail(Long postId, Long spaceMemberId) {
        QPostJpaEntity post = QPostJpaEntity.postJpaEntity;
        QPostBaseJpaEntity postBase = QPostBaseJpaEntity.postBaseJpaEntity;
        QSpaceMemberJpaEntity postCreator = QSpaceMemberJpaEntity.spaceMemberJpaEntity;
        QAttachmentJpaEntity attachment = QAttachmentJpaEntity.attachmentJpaEntity;
        QLikeJpaEntity postLike = QLikeJpaEntity.likeJpaEntity;

        // 1. 게시글 기본 정보를 조회 (첨부파일은 제외)
        PostDetailView detail = queryFactory
                .select(Projections.constructor(PostDetailView.class,
                        // 게시글 작성자 이름: 익명 여부에 따라 결정
                        Expressions.stringTemplate(
                                "CASE WHEN {0} = true THEN {1} ELSE {2} END",
                                post.isAnonymous,
                                Expressions.constant(ANONYMOUS_POST_CREATOR_NICKNAME),
                                postCreator.nickname),
                        // 게시글 작성자 프로필 이미지 URL
                        postCreator.profileImageUrl,
                        // 게시글 생성 시각
                        postBase.createdAt,
                        // 게시글 수정 시각
                        postBase.lastModifiedAt,
                        // 제목
                        post.title,
                        // 내용
                        postBase.content,
                        // 첨부파일 url list -> 빈 리스트
                        Expressions.constant(java.util.Collections.emptyList()),
                        // 좋아요 수 -> 서브쿼리로 집계
                        JPAExpressions.select(postLike.id.count())
                                .from(postLike)
                                .where(postLike.postBase.id.eq(postBase.id)
                                        .and(postLike.isLiked.eq(true))
                                        .and(postLike.status.eq(BaseStatusType.ACTIVE))),
                        // 스페이스 멤버가 해당 게시글을 좋아하는지
                        JPAExpressions.selectOne()
                                .from(postLike)
                                .where(postLike.postBase.id.eq(post.id)
                                        .and(postLike.spaceMember.id.eq(spaceMemberId))
                                        .and(postLike.isLiked.eq(true))
                                        .and(postLike.status.eq(BaseStatusType.ACTIVE)))
                                .exists()
                ))
                .from(post)
                .leftJoin(post.postBase, postBase)
                .leftJoin(postBase.spaceMember, postCreator)
                .where(post.id.eq(postId)
                        .and(postBase.status.eq(BaseStatusType.ACTIVE)))
                .fetchOne();

        if (detail == null) {
            throw new CustomException(POST_NOT_FOUND);
        }

        // 2. 첨부파일 URL 리스트를 별도로 조회
        List<String> attachmentUrls = queryFactory
                .select(attachment.attachmentUrl)
                .from(attachment)
                .where(attachment.postBase.id.eq(postId))
                .fetch();

        // 3. return
        return PostDetailView.builder()
                .creatorName(detail.getCreatorName())
                .creatorProfileImageUrl(detail.getCreatorProfileImageUrl())
                .createdAt(detail.getCreatedAt())
                .lastModifiedAt(detail.getLastModifiedAt())
                .title(detail.getTitle())
                .content(detail.getContent())
                .attachmentUrls(attachmentUrls)
                .likeCount(detail.getLikeCount())
                .isLiked(detail.getIsLiked())
                .build();
    }
}
