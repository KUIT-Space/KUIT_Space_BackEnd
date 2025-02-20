package space.space_spring.domain.post.adapter.out.persistence.post;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.attachment.AttachmentJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.attachment.QAttachmentJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.attachment.SpringDataAttachmentRepository;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.board.SpringDataBoardRepository;
import space.space_spring.domain.post.adapter.out.persistence.comment.QCommentJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.like.QLikeJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseMapper;
import space.space_spring.domain.post.adapter.out.persistence.postBase.QPostBaseJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.SpringDataPostBaseRepository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.application.port.in.readPostList.ListOfPostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.PostSummary;
import space.space_spring.domain.post.application.port.out.CreatePostPort;
import space.space_spring.domain.post.application.port.out.LoadPostPort;
import space.space_spring.domain.post.domain.AttachmentType;
import space.space_spring.domain.post.domain.Content;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpringDataSpaceMemberRepository;
import space.space_spring.domain.spaceMember.domian.QSpaceMemberJpaEntity;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.exception.CustomException;

import java.util.List;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class PostPersistenceAdapter implements CreatePostPort, LoadPostPort {

    private final SpringDataPostBaseRepository postBaseRepository;
    private final SpringDataPostRepository postRepository;
    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final SpringDataBoardRepository boardRepository;
    private final PostMapper postMapper;
    private final PostBaseMapper postBaseMapper;
    private final JPAQueryFactory queryFactory;

    @Override
    public Long createPost (Post post) {
        // 1. SpaceMember 조회
        SpaceMemberJpaEntity spaceMemberJpaEntity = spaceMemberRepository.findById(post.getSpaceMemberId())
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));

        // 2. Board 조회
        BoardJpaEntity boardJpaEntity = boardRepository.findById(post.getBoardId())
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));

        // 3. PostBaseJpaEntity 생성 및 저장
        PostBaseJpaEntity postBaseJpaEntity = postBaseMapper.toJpaEntity(post.getDiscordId(), spaceMemberJpaEntity, boardJpaEntity, post.getContent());
        PostBaseJpaEntity savedPostBase = postBaseRepository.save(postBaseJpaEntity);

        // 4. PostJpaEntity 생성 및 저장
        PostJpaEntity postJpaEntity = postMapper.toJpaEntity(savedPostBase, post);

        return postRepository.save(postJpaEntity).getId();
    }

    @Override
    public ListOfPostSummary loadPostList(Long boardId) {
        QPostJpaEntity post = QPostJpaEntity.postJpaEntity;
        QPostBaseJpaEntity postBase = QPostBaseJpaEntity.postBaseJpaEntity;
        QSpaceMemberJpaEntity spaceMember = QSpaceMemberJpaEntity.spaceMemberJpaEntity;
        QLikeJpaEntity like = QLikeJpaEntity.likeJpaEntity;
        QCommentJpaEntity comment = QCommentJpaEntity.commentJpaEntity;
        QAttachmentJpaEntity attachment = QAttachmentJpaEntity.attachmentJpaEntity;

        // QueryDSL로 데이터 조회
        List<Tuple> results = queryFactory
                .select(
                    post,
                    spaceMember,
                    like.countDistinct(),
                    comment.countDistinct(),
                    attachment.attachmentUrl.min() // 첫 번째 이미지 가져오기
                )
                .from(post)
                .join(post.postBase, postBase)
                .join(postBase.spaceMember, spaceMember)
                .leftJoin(like).on(like.postBase.eq(postBase))
                .leftJoin(comment).on(comment.postBase.eq(postBase))
                .leftJoin(attachment).on(attachment.postBase.eq(postBase)
                        .and(attachment.attachmentType.eq(AttachmentType.IMAGE)))
                .where(postBase.board.id.eq(boardId)) // boardId 기준 필터링
                .groupBy(post.id, spaceMember.id)
                .fetch();

        List<PostSummary> postSummaries = results.stream().map(tuple -> {
            PostJpaEntity postJpa = tuple.get(post);
            SpaceMemberJpaEntity spaceMemberJpa = tuple.get(spaceMember);
            Long likeCount = tuple.get(2, Long.class);
            Long commentCount = tuple.get(3, Long.class);
            String postImageUrl = tuple.get(4, String.class);

            return PostSummary.of(
                    postJpa.getId(),
                    postJpa.getTitle(),
                    new Content(postJpa.getPostBase().getContent()),
                    likeCount != null ? likeCount.intValue() : 0,
                    commentCount != null ? commentCount.intValue() : 0,
                    spaceMemberJpa.getNickname(),
                    postImageUrl
                );
        }).toList();

        return ListOfPostSummary.of(postSummaries);
    }
}
