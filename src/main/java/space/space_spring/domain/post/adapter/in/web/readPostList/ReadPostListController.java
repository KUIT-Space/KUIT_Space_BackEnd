package space.space_spring.domain.post.adapter.in.web.readPostList;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.readPostList.ReadPostListUseCase;
import space.space_spring.global.common.response.BaseResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReadPostListController {

    private final ReadPostListUseCase readPostListUseCase;

    @GetMapping("/board/{boardId}/post")
    public BaseResponse<List<ResponseOfReadPostList>> readPostList(@RequestParam Long boardId) {
        return new BaseResponse<>(readPostListUseCase.readPostList(boardId));
    }
}
