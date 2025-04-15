package space.space_spring.domain.post.adapter.in.web.readPostList;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.readPostList.ListOfPostSummary;
import space.space_spring.domain.post.application.port.in.readPostList.ReadPostListUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;


@RestController
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 관련 API")
public class ReadPostListController {

    private final ReadPostListUseCase readPostListUseCase;

    @Operation(summary = "게시글 리스트 조회", description = """
            
            스페이스 멤버가 게시판의 게시글 리스트를 조회합니다.(무한 스크롤)
            
            """)
    @GetMapping("/space/{spaceId}/board/{boardId}/post")
    public BaseResponse<ResponseOfReadPostList> readPostList(
            @JwtLoginAuth Long spaceMemberId,
            @PathVariable Long spaceId,
            @PathVariable Long boardId,
            @RequestParam(required = false) Long tagId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postBase.createdAt").descending());
        ListOfPostSummary postSummaries = readPostListUseCase.readPostList(spaceMemberId, boardId, tagId, pageable);
        return new BaseResponse<>(ResponseOfReadPostList.of(postSummaries));
    }
}
