package space.space_spring.domain.post.adapter.out.persistence.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.board.SpringDataBoardRepository;
import space.space_spring.domain.post.adapter.out.persistence.post.PostJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.post.PostRepository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseMapper;
import space.space_spring.domain.post.adapter.out.persistence.postBase.SpringDataPostBaseRepository;
import space.space_spring.domain.post.application.port.out.CreateCommentPort;
import space.space_spring.domain.post.application.port.out.DeleteCommentPort;
import space.space_spring.domain.post.application.port.out.LoadCommentPort;
import space.space_spring.domain.post.application.port.out.UpdateCommentPort;
import space.space_spring.domain.post.domain.Comment;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpaceMemberRepository;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.util.NaturalNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements LoadCommentPort, CreateCommentPort, UpdateCommentPort, DeleteCommentPort {

    private final SpaceMemberRepository spaceMemberRepository;
    private final SpringDataBoardRepository boardRepository;
    private final SpringDataPostCommentRepository postCommentRepository;
    private final PostRepository postRepository;
    private final SpringDataPostBaseRepository postBaseRepository;
    private final CommentMapper commentMapper;
    private final PostBaseMapper postBaseMapper;

    @Override
    public Map<Long, NaturalNumber> countCommentsByPostIds(List<Long> postIds) {
        List<PostCommentCount> results = postCommentRepository.countCommentsByPostIds(postIds);
        return results.stream()
                .collect(Collectors.toMap(
                        PostCommentCount::getPostId,
                        postCommentCount -> NaturalNumber.of(postCommentCount.getCommentCount().intValue())
                ));
    }

    @Override
    public Comment loadById(Long commentId) {
        // Comment 에 해당하는 jpa entity 찾기
        PostCommentJpaEntity postCommentJpaEntity = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        if (!postCommentJpaEntity.getPostBase().isActive()) {
            throw new CustomException(COMMENT_NOT_FOUND);          // 찾은 Comment가 Active 상태가 아닌 경우
        }

        return commentMapper.toDomainEntity(postCommentJpaEntity);
    }

    @Override
    public List<Comment> loadAllComments(Long postId) {
        List<PostCommentJpaEntity> commentJpaEntities = postCommentRepository.findByPostId(postId);

        List<Comment> comments = new ArrayList<>();
        for (PostCommentJpaEntity postCommentJpaEntity : commentJpaEntities) {
            if (postCommentJpaEntity.getPostBase().isActive()) {
                comments.add(commentMapper.toDomainEntity(postCommentJpaEntity));
            }
        }

        return comments;
    }

    @Override
    public Comment loadByDiscordId(Long discordMessageId) {
        PostBaseJpaEntity postBaseJpaEntity = postBaseRepository.findByDiscordIdAndStatus(discordMessageId, BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(POST_BASE_NOT_FOUND));

        PostCommentJpaEntity postCommentJpaEntity = postCommentRepository.findById(postBaseJpaEntity.getId())
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        return commentMapper.toDomainEntity(postCommentJpaEntity);
    }

    @Override
    public Long createComment(Comment comment) {
        // Post 에 해당하는 jpa entity 찾기
        PostJpaEntity postJpaEntity = postRepository.findById(comment.getPostId())
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        if (!postJpaEntity.getPostBase().isActive()) {
            throw new CustomException(POST_NOT_FOUND);          // 찾은 Post가 Active 상태가 아닌 경우
        }

        // Comment에 해당하는 postBaseJpaEntity 생성
        SpaceMemberJpaEntity commentCreator = spaceMemberRepository.findByIdAndStatus(comment.getCommentCreatorId(), BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(SPACE_MEMBER_NOT_FOUND));
        BoardJpaEntity boardJpaEntity = boardRepository.findByIdAndStatus(comment.getBoardId(), BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(BOARD_NOT_FOUND));
        PostBaseJpaEntity commentPostBase = postBaseMapper.toJpaEntity(commentCreator, boardJpaEntity, comment);

        // commentJpaEntity 생성 & 저장
        return postCommentRepository.save(commentMapper.toJpaEntity(commentPostBase, postJpaEntity, comment)).getId();
    }

    @Override
    public void updateComment(Comment comment) {
        // Comment 에 해당하는 jpa entity 찾기
        PostCommentJpaEntity postCommentJpaEntity = postCommentRepository.findById(comment.getId())
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        if (!postCommentJpaEntity.getPostBase().isActive()) {
            throw new CustomException(COMMENT_NOT_FOUND);          // 찾은 Comment가 Active 상태가 아닌 경우
        }

        // jpa entity 필드 속성 update
        postCommentJpaEntity.getPostBase().changeContent(comment.getContent());
    }

    /**
     * 댓글 삭제는 soft delete -> 삭제된 댓글을 보여줄때는 "삭제된 댓글입니다." 라고 표시
     */
    @Override
    public void deleteComment(Long commentId) {
        // Comment 에 해당하는 jpa entity 찾기
        PostCommentJpaEntity postCommentJpaEntity = postCommentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_FOUND));

        if (!postCommentJpaEntity.getPostBase().isActive()) {
            throw new CustomException(COMMENT_NOT_FOUND);          // 찾은 Comment가 Active 상태가 아닌 경우
        }

        // jpa entity 를 INACTIVE 상태로 변경
        postCommentJpaEntity.getPostBase().updateToInactive();
    }

    /**
     * status 상관없이 post에 달린 모든 comment 들 조회
     */
    @Override
    public List<Comment> loadByPostIdWithoutStatusFilter(Long postId) {
        Optional<List<PostCommentJpaEntity>> allByPostId = postCommentRepository.findAllByPostId(postId);

        if (allByPostId.isEmpty()) return new ArrayList<>();

        List<Comment> comments = new ArrayList<>();
        for (PostCommentJpaEntity postCommentJpaEntity : allByPostId.get()) {
            comments.add(commentMapper.toDomainEntity(postCommentJpaEntity));
        }

        return comments;

    }

    @Override
    public void deleteAllComments(List<Comment> comments) {
        if (comments.isEmpty()) {
            return;
        }

        List<Long> commentIds = comments.stream()
                .map(Comment::getId)
                .toList();

        List<PostCommentJpaEntity> commentEntities = postCommentRepository.findAllById(commentIds);

        for (PostCommentJpaEntity comment : commentEntities) {
            comment.getPostBase().updateToInactive(); // Soft Delete 적용
        }
    }
}