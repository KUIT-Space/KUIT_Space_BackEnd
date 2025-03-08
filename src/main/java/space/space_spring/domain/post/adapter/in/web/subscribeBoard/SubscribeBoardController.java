package space.space_spring.domain.post.adapter.in.web.subscribeBoard;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.subscribeBoard.SubscribeBoardUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}")
@Tag(name = "Board", description = "게시판 관련 API")
public class SubscribeBoardController {

    private final SubscribeBoardUseCase subscribeBoardUseCase;

    @Operation(summary = "게시판 구독", description = """
        
        사용자가 게시판을 구독합니다.
        
        """)
    @PostMapping("/board/subscribe/{boardId}")
    public BaseResponse<SuccessResponse> subscribeEvent(@JwtLoginAuth Long spaceMemberId, @PathVariable Long boardId) {
        subscribeBoardUseCase.subscribe(spaceMemberId, boardId);
        return new BaseResponse<>(new SuccessResponse(true));
    }

    @Operation(summary = "게시판 구독 취소", description = """
        
        사용자가 게시판 구독을 취소합니다.
        
        """)
    @DeleteMapping("/board/subscribe/{boardId}")
    public BaseResponse<SuccessResponse> unsubscribeEvent(@JwtLoginAuth Long spaceMemberId, @PathVariable Long boardId) {
        subscribeBoardUseCase.unsubscribe(spaceMemberId, boardId);
        return new BaseResponse<>(new SuccessResponse(true));
    }

}
