package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import space.space_spring.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.LikeService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/board/post/{postId}")
@Slf4j
public class LikeController {

    private final LikeService likeService;

    // 게시글 좋아요
    @PostMapping("/like")
    public BaseResponse<String> likePost(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long postId
    ) {
        // TODO 1: 유저가 스페이스에 속하는지 검증
        likeService.validateUserInSpace(userId, spaceId);

        // TODO 2: 유저가 해당 게시글에 이미 좋아요를 눌렀는지 검증
        likeService.validateAlreadyLiked(userId, postId);

        // TODO 3: 좋아요 로직 수행
        likeService.likePost(userId, postId);

        return new BaseResponse<>("게시글에 좋아요를 눌렀습니다.");
    }


    // 게시글 좋아요 취소
    @DeleteMapping("/like")
    public BaseResponse<String> unlikePost(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long postId
    ) {
        // TODO 1: 유저가 스페이스에 속하는지 검증
        likeService.validateUserInSpace(userId, spaceId);

        // TODO 2: 유저가 해당 게시글에 좋아요를 눌렀는지 검증
        likeService.validateNotLikedYet(userId, postId);

        // TODO 3: 좋아요 취소 로직 수행
        likeService.unlikePost(userId, postId);

        return new BaseResponse<>("게시글에 좋아요를 취소하였습니다.");
    }
}
