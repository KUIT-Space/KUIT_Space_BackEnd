package space.space_spring.domain.post.adapter.in.web.readPostList;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.readPostList.ListOfPostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.ReadPostListUseCase;
import space.space_spring.global.common.response.BaseResponse;


@RestController
@RequiredArgsConstructor
public class ReadPostListController {

    private final ReadPostListUseCase readPostListUseCase;

    @GetMapping("/space/{spaceId}/board/{boardId}/post")
    public BaseResponse<ResponseOfReadPostList> readPostList(
            @PathVariable Long spaceId,
            @PathVariable Long boardId,
            @RequestParam(required = false) Long tagId) {
        ListOfPostSummary postSummaries = readPostListUseCase.readPostList(boardId, tagId);
        return new BaseResponse<>(ResponseOfReadPostList.of(postSummaries));
    }
}
