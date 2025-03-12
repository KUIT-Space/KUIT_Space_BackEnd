package space.space_spring.domain.post.adapter.in.web.changeLikeState;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.changeLikeState.ChangeLikeStateCommand;
import space.space_spring.domain.post.application.port.in.changeLikeState.ChangeLikeStateUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_CHANGE_LIKE_STATE_REQUEST;

@RestController
@RequiredArgsConstructor
@Tag(name = "Like", description = "좋아요 관련 API")
public class ChangeLikeStateController {

    private final ChangeLikeStateUseCase changeLikeStateUseCase;

    @Operation(summary = "좋아요 on/off", description = """
                        
            스페이스 멤버가 게시글/댓글에 좋아요를 ON or OFF 합니다.
                        
            """)
    @PatchMapping("/space/{spaceId}/board/{boardId}/target/{targetId}/like-state")
    public BaseResponse<SuccessResponse> changeLikeState(@JwtLoginAuth Long spaceMemberId,
                                                         @PathVariable("spaceId") Long spaceId, @PathVariable("boardId") Long boardId, @PathVariable("targetId") Long targetId,
                                                         @Validated @RequestBody RequestOfChangeLikeState request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_CHANGE_LIKE_STATE_REQUEST);
        }

        ChangeLikeStateCommand command = ChangeLikeStateCommand.builder()
                .spaceId(spaceId)
                .boardId(boardId)
                .targetId(targetId)
                .spaceMemberId(spaceMemberId)
                .changeTo(request.getChangeTo())
                .build();

        changeLikeStateUseCase.changeLikeState(command);

        return new BaseResponse<>(new SuccessResponse(true));
    }
}
