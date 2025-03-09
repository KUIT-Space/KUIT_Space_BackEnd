package space.space_spring.domain.post.adapter.in.web.readPostDetail;

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
public class ReadPostDetailController {

    private final ReadPostDetailUseCase readPostDetailUseCase;

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
