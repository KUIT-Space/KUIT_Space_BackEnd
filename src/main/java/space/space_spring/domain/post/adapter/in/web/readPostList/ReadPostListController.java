package space.space_spring.domain.post.adapter.in.web.readPostList;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.readPostList.ListOfPostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.ReadPostListUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;


@RestController
@RequiredArgsConstructor
public class ReadPostListController {

    private final ReadPostListUseCase readPostListUseCase;

    @GetMapping("/space/{spaceId}/board/{boardId}/post")
    public BaseResponse<ResponseOfReadPostList> readPostList(@JwtLoginAuth Long id, @RequestParam Long boardId) {
        ListOfPostSummary postSummaries = readPostListUseCase.readPostList(boardId);
        return new BaseResponse<>(ResponseOfReadPostList.of(postSummaries));
    }
}
