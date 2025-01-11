package space.space_spring.domain.board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.argumentResolver.userSpace.CheckUserSpace;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.domain.board.service.LikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/board/post/{postId}")
@Slf4j
public class LikeController {

    private final LikeService likeService;

    // 게시글 좋아요
    @PostMapping("/like")
    @CheckUserSpace(required = false)
    public BaseResponse<String> likePost(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long postId
    ) {
        // TODO 1: 유저가 해당 게시글에 좋아요를 눌렀는지 검증
        likeService.validateAlreadyLikedPost(userId, postId);

        // TODO 2: 좋아요 로직 수행
        likeService.likePost(userId, postId);

        return new BaseResponse<>("게시글에 좋아요를 눌렀습니다.");
    }


    // 게시글 좋아요 취소
    @DeleteMapping("/like")
    @CheckUserSpace(required = false)
    public BaseResponse<String> unlikePost(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long postId
    ) {

        // TODO 1: 유저가 해당 게시글에 좋아요를 눌렀는지 검증
        likeService.validateNotLikedPost(userId, postId);

        // TODO 2: 좋아요 취소 로직 수행
        likeService.unlikePost(userId, postId);

        return new BaseResponse<>("게시글에 좋아요를 취소하였습니다.");
    }

    // 댓글 좋아요
    @PostMapping("/comment/{commentId}/like")
    @CheckUserSpace(required = false)
    public BaseResponse<String> likeComment(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {

        // TODO 1: 댓글이 게시글에 속하는지 검증
        likeService.validateCommentInPost(postId, commentId);

        // TODO 2: 유저가 해당 댓글에 좋아요를 눌렀는지 검증
        likeService.validateAlreadyLikedComment(userId, commentId);

        // TODO 3: 좋아요 로직 수행
        likeService.likeComment(userId, commentId);


        return new BaseResponse<>("댓글에 좋아요를 눌렀습니다.");
    }



    // 댓글 좋아요 취소
    @DeleteMapping("/comment/{commentId}/like")
    @CheckUserSpace(required = false)
    public BaseResponse<String> unlikeComment(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ) {

        // TODO 1: 댓글이 게시글에 속하는지 검증
        likeService.validateCommentInPost(postId, commentId);

        // TODO 2: 유저가 해당 댓글에 좋아요를 눌렀는지 검증
        likeService.validateNotLikedComment(userId, commentId);

        // TODO 3: 좋아요 취소 로직 수행
        likeService.unlikeComment(userId, commentId);


        return new BaseResponse<>("댓글에 좋아요를 취소하였습니다.");
    }

}
