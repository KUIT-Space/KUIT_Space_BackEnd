package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.space_spring.argument_resolver.jwtLogin.JwtLoginAuth;
import space.space_spring.dto.post.request.CreatePostRequest;
import space.space_spring.dto.post.response.ReadPostDetailResponse;
import space.space_spring.dto.post.response.ReadPostsResponse;
import space.space_spring.entity.UserSpace;
import space.space_spring.exception.CustomException;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.PostService;
import space.space_spring.util.userSpace.UserSpaceUtils;
import java.util.List;
import java.util.Optional;

import static space.space_spring.response.status.BaseExceptionResponseStatus.INVALID_POST_CREATE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}")
@Slf4j
public class PostController {
    private final PostService postService;
    private final UserSpaceUtils userSpaceUtils;

    // 게시글 조회
    @GetMapping("/board")
    public BaseResponse<List<ReadPostsResponse>> getBoard(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @RequestParam(defaultValue = "all") String filter) {

        // TODO 1: 유저가 스페이스에 속하는지 검증
        Optional<UserSpace> userInSpace = userSpaceUtils.isUserInSpace(userId, spaceId);
        log.info("UserName = {}, UserSpaceAuth = {}", userInSpace.get().getUserName(), userInSpace.get().getUserSpaceAuth());

        // TODO 2: 게시글 필터링 적용하여 리스트 get
        List<ReadPostsResponse> board = postService.getAllPosts(spaceId, filter, userId);
        return new BaseResponse<>(board);
    }

    // 게시글 작성
    @PostMapping("/board/post")
    public BaseResponse<String> createPost(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @ModelAttribute @Validated CreatePostRequest createPostRequest,
            BindingResult bindingResult) {
        // TODO 1: 예외처리
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_POST_CREATE);
        }
        // TODO 2: 유저가 스페이스에 속하는 지 검증
        Optional<UserSpace> userInSpace = userSpaceUtils.isUserInSpace(userId, spaceId);
        log.info("UserName = {}, UserSpaceAuth = {}", userInSpace.get().getUserName(), userInSpace.get().getUserSpaceAuth());

        // TODO 3: 작성한 게시글 save 작업 수행
        postService.save(userId, spaceId, createPostRequest);

        return new BaseResponse<>("새로운 글이 작성되었습니다.");
    }

    // 게시글 상세 조회
    @GetMapping("/board/post/{postId}")
    public BaseResponse<ReadPostDetailResponse> getPost(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long postId) {

        // TODO 1: 유저가 스페이스에 속하는지 검증
        Optional<UserSpace> userInSpace = userSpaceUtils.isUserInSpace(userId, spaceId);
        log.info("UserName = {}, UserSpaceAuth = {}", userInSpace.get().getUserName(), userInSpace.get().getUserSpaceAuth());

        // TODO 2: 게시글 상세 정보 가져오기
        ReadPostDetailResponse readPostDetailResponse = postService.getPostDetail(userId, spaceId, postId);

        return new BaseResponse<>(readPostDetailResponse);
    }
}
