package space.space_spring.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.space_spring.dao.CommentDao;
import space.space_spring.dao.CommentLikeDao;
import space.space_spring.dao.PostLikeDao;
import space.space_spring.dao.PostDao;
import space.space_spring.entity.*;
import space.space_spring.exception.CustomException;
import space.space_spring.util.user.UserUtils;
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.util.Optional;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {
    private final PostLikeDao postLikeDao;
    private final CommentLikeDao commentLikeDao;
    private final PostDao postDao;
    private final CommentDao commentDao;
    private final UserUtils userUtils;
    private final UserSpaceUtils userSpaceUtils;

    // TODO 1: 댓글이 해당 게시글에 속하는지 검증
    public void validateCommentInPost(Long postId, Long commentId) {
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_EXIST));

        Comment comment = commentDao.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_EXIST));

        if(!comment.getPost().getPostId().equals(postId)) {
            throw new CustomException(COMMENT_IS_NOT_IN_POST);
        }
    }

    // TODO 2: 유저가 해당 게시글에 좋아요를 눌렀는지 검증 (좋아요 중복 방지)
    public void validateAlreadyLikedPost(Long userId, Long postId) {
        User user = userUtils.findUserByUserId(userId);
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_EXIST));

        Optional<PostLike> existingLike = postLikeDao.findByUserAndPost(user, post);

        if(existingLike.isPresent()) {
            throw new CustomException(ALREADY_LIKED_THE_POST);
        }
    }

    // TODO 3: 유저가 해당 게시글에 좋아요를 눌렀는지 검증 (취소 중복 방지)
    public void validateNotLikedPost(Long userId, Long postId) {
        User user = userUtils.findUserByUserId(userId);
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_EXIST));

        Optional<PostLike> existingLike = postLikeDao.findByUserAndPost(user, post);

        if(existingLike.isEmpty()) {
            throw new CustomException(NOT_LIKED_THE_POST_YET);
        }
    }

    // TODO 4: 유저가 해당 댓글에 좋아요를 눌렀는지 검증 (좋아요 중복 방지)
    public void validateAlreadyLikedComment(Long userId, Long commentId) {
        User user = userUtils.findUserByUserId(userId);
        Comment comment = commentDao.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_EXIST));

        Optional<CommentLike> existingLike = commentLikeDao.findByUserAndComment(user, comment);

        if(existingLike.isPresent()) {
            throw new CustomException(ALREADY_LIKED_THE_COMMENT);
        }
    }

    // TODO 5: 유저가 해당 댓글에 좋아요를 눌렀는지 검증 (취소 중복 방지)
    public void validateNotLikedComment(Long userId, Long commentId) {
        User user = userUtils.findUserByUserId(userId);
        Comment comment = commentDao.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_EXIST));

        Optional<CommentLike> existingLike = commentLikeDao.findByUserAndComment(user, comment);

        if(existingLike.isEmpty()) {
            throw new CustomException(NOT_LIKED_THE_COMMENT_YET);
        }
    }

    @Transactional
    public void likePost(Long userId, Long postId) {
        // 유저와 게시글 조회
        User user = userUtils.findUserByUserId(userId);
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_EXIST));

        // 새 좋아요 생성 및 저장
        PostLike postLike = new PostLike(user, post);
        postLikeDao.save(postLike);

        // 게시글의 좋아요 수 증가
        post.increaseLikeCount();
    }

    @Transactional
    public void unlikePost(Long userId, Long postId) {
        // 유저와 게시글 조회
        User user = userUtils.findUserByUserId(userId);
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_EXIST));

        // 좋아요 취소 및 삭제
        Optional<PostLike> existingLike = postLikeDao.findByUserAndPost(user, post);
        postLikeDao.delete(existingLike.get());

        // 게시글의 좋아요 수 감소
        post.decreaseLikeCount();
    }

    @Transactional
    public void likeComment(Long userId, Long commentId) {
        // 유저와 댓글 조회
        User user = userUtils.findUserByUserId(userId);
        Comment comment = commentDao.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_EXIST));

        // 좋아요 생성 및 저장
        CommentLike commentLike = new CommentLike(user, comment);
        commentLikeDao.save(commentLike);

        // 게시글의 좋아요 수 증가
        comment.increaseLikeCount();
    }

    @Transactional
    public void unlikeComment(Long userId, Long commentId) {
        // 유저와 게시글 조회
        User user = userUtils.findUserByUserId(userId);
        Comment comment = commentDao.findById(commentId)
                .orElseThrow(() -> new CustomException(COMMENT_NOT_EXIST));

        // 좋아요 취소 및 삭제
        Optional<CommentLike> existingLike = commentLikeDao.findByUserAndComment(user, comment);
        commentLikeDao.delete(existingLike.get());

        // 게시글의 좋아요 수 감소
        comment.decreaseLikeCount();
    }
}
