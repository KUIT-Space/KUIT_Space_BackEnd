package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import space.space_spring.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.dto.post.ReadPostsResponse;
import space.space_spring.entity.UserSpace;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.PostService;
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}")
@Slf4j
public class PostController {
    private final PostService postService;
    private final UserSpaceUtils userSpaceUtils;

    @GetMapping("/board")
    public BaseResponse<List<ReadPostsResponse>> getBoard(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @RequestParam(defaultValue = "all") String filter) {
        Optional<UserSpace> userInSpace = userSpaceUtils.isUserInSpace(userId, spaceId);
        log.info("UserName = {}, UserSpaceAuth = {}", userInSpace.get().getUserName(), userInSpace.get().getUserSpaceAuth());
        List<ReadPostsResponse> board = postService.getAllPosts(spaceId, filter);
        return new BaseResponse<>(board);
    }

}
