package space.space_spring.domain.post.adapter.out.persistence.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import space.space_spring.domain.post.adapter.out.persistence.post.PostJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.post.SpringDataPostRepository;
import space.space_spring.domain.post.adapter.out.persistence.postBase.PostBaseJpaEntity;
import space.space_spring.domain.post.adapter.out.persistence.postBase.SpringDataPostBaseRepository;
import space.space_spring.domain.post.application.port.out.CreateCommentPort;
import space.space_spring.domain.post.application.port.out.LoadCommentPort;
import space.space_spring.domain.post.domain.Comment;
import space.space_spring.global.common.enumStatus.BaseStatusType;
import space.space_spring.global.exception.CustomException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.POST_BASE_NOT_FOUND;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.POST_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class CommentPersistenceAdapter implements LoadCommentPort, CreateCommentPort {

    private final SpringDataPostCommentRepository postCommentRepository;
    private final SpringDataPostBaseRepository postBaseRepository;
    private final SpringDataPostRepository postRepository;
    private final CommentMapper commentMapper;

    @Override
    public Map<Long, Long> countCommentsByPostIds(List<Long> postIds) {
        List<PostCommentCount> results = postCommentRepository.countCommentsByPostIds(postIds);
        return results.stream()
                .collect(Collectors.toMap(PostCommentCount::getPostId, PostCommentCount::getCommentCount));
    }

    @Override
    public Long createComment(Comment comment) {
        PostBaseJpaEntity postBaseJpaEntity = postBaseRepository.findByIdAndStatus(comment.getTargetId(), BaseStatusType.ACTIVE)
                .orElseThrow(() -> new CustomException(POST_BASE_NOT_FOUND));

        PostJpaEntity postJpaEntity = postRepository.findByPostBaseId(comment.getTargetId())
                .orElseThrow(() -> new CustomException(POST_NOT_FOUND));

        return postCommentRepository.save(commentMapper.toJpaEntity(postBaseJpaEntity, postJpaEntity, comment)).getId();
    }
}
