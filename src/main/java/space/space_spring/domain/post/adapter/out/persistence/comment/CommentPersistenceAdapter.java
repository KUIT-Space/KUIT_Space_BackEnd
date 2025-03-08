package space.space_spring.domain.post.adapter.out.persistence.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.board.BoardJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.board.SpringDataBoardRepository;
import space.space_spring.domain.post.adapter.out.persistence.post.PostJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.post.SpringDataPostRepository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseMapper;
import space.space_spring.domain.post.adapter.out.persistence.postBase.SpringDataPostBaseRepository;
import space.space_spring.domain.post.application.port.out.CreateCommentPort;
import space.space_spring.domain.post.application.port.out.LoadCommentPort;
import space.space_spring.domain.post.application.port.out.UpdateCommentPort;
import space.space_spring.domain.post.domain.Comment;
import space.space_spring.domain.spaceMember.adapter.out.persistence.SpringDataSpaceMemberRepository;
import space.space_spring.domain.spaceMember.domian.SpaceMemberJpaEntity;
import space.space_spring.global.common.enumStatus.BaseStatusType;
import space.space_spring.global.exception.CustomException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

@Repository
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements LoadCommentPort, CreateCommentPort, UpdateCommentPort {

    private final SpringDataSpaceMemberRepository spaceMemberRepository;
    private final SpringDataBoardRepository boardRepository;
    private final SpringDataPostCommentRepository postCommentRepository;
    private final SpringDataPostBaseRepository postBaseRepository;
    private final SpringDataPostRepository postRepository;
    private final CommentMapper commentMapper;
    private final PostBaseMapper postBaseMapper;

    @Override
    public Map<Long, Long> countCommentsByPostIds(List<Long> postIds) {
        List<PostCommentCount> results = postCommentRepository.countCommentsByPostIds(postIds);
        return results.stream()
                .collect(Collectors.toMap(PostCommentCount::getPostId, PostCommentCount::getCommentCount));
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
        postCommentJpaEntity.getPostBase().changeContent(comment.getContent().getValue());
        postCommentJpaEntity.changeAnonymous(comment.isAnonymous());
    }
}
