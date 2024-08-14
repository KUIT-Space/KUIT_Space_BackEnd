package space.space_spring.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import space.space_spring.dao.LikeDao;
import space.space_spring.dao.PostDao;
import space.space_spring.entity.Post;
import space.space_spring.entity.PostLike;
import space.space_spring.entity.User;
import space.space_spring.exception.CustomException;
import space.space_spring.util.user.UserUtils;
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.util.Optional;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {
    private final LikeDao likeDao;
    private final PostDao postDao;
    private final UserUtils userUtils;
    private final UserSpaceUtils userSpaceUtils;

    // TODO 1: 유저가 스페이스에 속하는지 검증
    public void validateUserInSpace(Long userId, Long spaceId) {
        if (userSpaceUtils.isUserInSpace(userId, spaceId).isEmpty()) {
            throw new CustomException(USER_IS_NOT_IN_SPACE);
        }
    }

    // TODO 2: 유저가 해당 게시글에 이미 좋아요를 눌렀는지 검증
    public void validateAlreadyLiked(Long userId, Long postId) {
        User user = userUtils.findUserByUserId(userId);
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_EXIST));

        Optional<PostLike> existingLike = likeDao.findByUserAndPost(user, post);

        if(existingLike.isPresent()) {
            throw new CustomException(ALREADY_LIKED_THE_POST);
        }
    }

    // TODO 3: 유저가 해당 게시글에 좋아요를 눌렀는지 검증
    public void validateNotLikedYet(Long userId, Long postId) {
        User user = userUtils.findUserByUserId(userId);
        Post post = postDao.findById(postId)
                .orElseThrow(() -> new CustomException(POST_NOT_EXIST));

        Optional<PostLike> existingLike = likeDao.findByUserAndPost(user, post);

        if (existingLike.isEmpty()) {
            throw new CustomException(NOT_LIKED_THE_POST_YET);
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
        likeDao.save(postLike);

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
        Optional<PostLike> existingLike = likeDao.findByUserAndPost(user, post);
        likeDao.delete(existingLike.get());

        // 게시글의 좋아요 수 감소
        post.decreaseLikeCount();
    }
}
