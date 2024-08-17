package space.space_spring.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.space_spring.dao.CommentDao;
import space.space_spring.dao.PostDao;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dto.comment.request.CreateCommentRequest;
import space.space_spring.dto.comment.response.ReadCommentsResponse;
import space.space_spring.entity.Post;
import space.space_spring.entity.Comment;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;
import space.space_spring.exception.CustomException;
import space.space_spring.util.user.UserUtils;
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final PostDao postDao;
    private final CommentDao commentDao;
    private final UserSpaceDao userSpaceDao;
    private final UserUtils userUtils;
    private final UserSpaceUtils userSpaceUtils;

    // TODO 1: 유저가 스페이스에 속하는지 검증
    public void validateUserInSpace(Long userId, Long spaceId) {
        if (userSpaceUtils.isUserInSpace(userId, spaceId).isEmpty()) {
            throw new CustomException(USER_IS_NOT_IN_SPACE);
        }
    }

    @Transactional
    public List<ReadCommentsResponse> getCommentsByPost(Long postId, Long userId) {
        // TODO 1: postId에 해당하는 post find
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_EXIST));

        // TODO 2: 해당 게시글의 모든 댓글 조회
        List<Comment> comments = commentDao.findByPost(post);

        // TODO 3: CommentResponse 리스트로 변환
        return comments.stream()
                .map(comment -> {
                    int commentCount = commentDao.countByTargetId(comment.getCommentId());
                    boolean isLike = commentDao.isUserLikedComment(comment.getCommentId(), userId);
                    Optional<UserSpace> userSpace = userSpaceDao.findUserSpaceByUserAndSpace(comment.getUser(), post.getSpace());

                    return ReadCommentsResponse.of(comment, userSpace.orElse(null), isLike, commentCount);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createComment(Long userId, Long postId, CreateCommentRequest.Request createCommentRequest) {
        // TODO 1: isReply와 targetId 유효성 검사
        if(createCommentRequest.isReply() && createCommentRequest.getTargetId() == null) {
            throw new CustomException(INVALID_REPLY_TARGET);
        }
        if(!createCommentRequest.isReply() && createCommentRequest.getTargetId() != null) {
            throw new CustomException(INVALID_COMMENT_TARGET);
        }

        // TODO 1: userId에 해당하는 user find
        User user = userUtils.findUserByUserId(userId);

        // TODO 2: postId에 해당하는 post find
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_EXIST));

        // TODO 3: 댓글 또는 대댓글 생성
        Comment comment = createCommentRequest.toEntity(user, post);
        commentDao.save(comment);

        return comment.getCommentId();
    }
}
