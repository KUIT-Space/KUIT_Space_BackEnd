package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.argument_resolver.jwtLogin.JwtLoginAuth;
import space.space_spring.dto.post.ReadBoardResponse;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.BoardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}")
@Slf4j
public class BoardController {
    private final BoardService boardService;

    @GetMapping("/board")
    public BaseResponse<List<ReadBoardResponse>> getBoard(@PathVariable Long spaceId) {
        List<ReadBoardResponse> board = boardService.getAllPosts(spaceId);
        return new BaseResponse<>(board);
    }

}
