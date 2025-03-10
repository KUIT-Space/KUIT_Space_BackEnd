package space.space_spring.domain.post.adapter.in.web.subscribeBoard;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_BOARD_SUBSCRIBE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.loadBoard.SubscribeBoardCommand;
import space.space_spring.domain.post.application.port.in.subscribeBoard.SubscribeBoardUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;
import space.space_spring.global.exception.CustomException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}")
@Tag(name = "Board", description = "게시판 관련 API")
public class SubscribeBoardController {

    private final SubscribeBoardUseCase subscribeBoardUseCase;

    @Operation(summary = "게시판 구독 등록", description = """
        
        사용자가 게시판을 구독합니다.
        
        """)
    @PostMapping("/board/subscribe")
    public BaseResponse<SuccessResponse> subscribeBoard(@JwtLoginAuth Long spaceMemberId, @PathVariable String spaceId, @Validated @RequestBody SubscribeBoardRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_BOARD_SUBSCRIBE);
        }

        SubscribeBoardCommand subscribeBoardCommand = SubscribeBoardCommand.builder()
                .spaceMemberId(spaceMemberId)
                .boardId(request.getBoardId())
                .tagName(request.getTagName())
                .build();
        subscribeBoardUseCase.subscribeBoard(subscribeBoardCommand);
        return new BaseResponse<>(new SuccessResponse(true));
    }

    @Operation(summary = "게시판 구독 취소", description = """
        
        사용자가 게시판 구독을 취소합니다.
        
        """)
    @PostMapping("/board/unsubscribe")
    public BaseResponse<SuccessResponse> unsubscribeBoard(@JwtLoginAuth Long spaceMemberId, @PathVariable String spaceId, @Validated @RequestBody SubscribeBoardRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_BOARD_SUBSCRIBE);
        }

        SubscribeBoardCommand subscribeBoardCommand = SubscribeBoardCommand.builder()
                .spaceMemberId(spaceMemberId)
                .boardId(request.getBoardId())
                .tagName(request.getTagName())
                .build();
        subscribeBoardUseCase.unsubscribeBoard(subscribeBoardCommand);
        return new BaseResponse<>(new SuccessResponse(true));
    }
}
