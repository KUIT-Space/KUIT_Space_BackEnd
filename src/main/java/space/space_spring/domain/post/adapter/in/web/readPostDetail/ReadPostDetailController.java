package space.space_spring.domain.post.adapter.in.web.readPostDetail;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.readPostDetail.ReadPostDetailCommand;
import space.space_spring.domain.post.application.port.in.readPostDetail.ReadPostDetailUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시판 관련 API")
public class ReadPostDetailController {

    private final ReadPostDetailUseCase readPostDetailUseCase;

    @Operation(summary = "게시글 상세 조회", description = """
                        
            스페이스 멤버가 특정 게시글과 해당 게시글에 달린 모든 댓글들을 조회합니다.
                        
            """)
    @GetMapping("/space/{spaceId}/board/{boardId}/post/{postId}")
    public BaseResponse<ResponseOfReadPostDetail> readPostDetail(@JwtLoginAuth Long spaceMemberId,
                                                                 @PathVariable("spaceId") Long spaceId, @PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId) {

        ReadPostDetailCommand command = ReadPostDetailCommand.builder()
                                            .spaceId(spaceId)
                                            .boardId(boardId)
                                            .postId(postId)
                                            .spaceMemberId(spaceMemberId)
                                            .build();
        return new BaseResponse<>(ResponseOfReadPostDetail.of(readPostDetailUseCase.readPostDetail(command)));
    }
}
