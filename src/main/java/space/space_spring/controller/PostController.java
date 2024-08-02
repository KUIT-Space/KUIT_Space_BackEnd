package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.dto.post.ReadPostsResponse;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.PostService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}")
@Slf4j
public class PostController {
    private final PostService postService;

    @GetMapping("/board")
    public BaseResponse<List<ReadPostsResponse>> getBoard(@PathVariable Long spaceId) {
        List<ReadPostsResponse> board = postService.getAllPosts(spaceId);
        return new BaseResponse<>(board);
    }

}
