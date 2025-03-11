package space.space_spring.domain.post.adapter.in.web.readBoardList;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.readBoardList.ReadBoardListCommand;
import space.space_spring.domain.post.application.port.in.readBoardList.ReadBoardListUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시판 관련 API")
public class ReadBoardListController {

    private final ReadBoardListUseCase readBoardListUseCase;

    @Operation(summary = "게시판 목록 조회", description = """
            
            사용자가 속한 스페이스 게시판 목록을 조회합니다.
            
            """)
    @GetMapping("/space/{spaceId}/board/list")
    public BaseResponse<ResponseOfReadBoardList> readBoardList(
            @JwtLoginAuth Long spaceMemberId,
            @PathVariable Long spaceId) {
        ReadBoardListCommand boardList = readBoardListUseCase.readBoardList(spaceMemberId, spaceId);
        return new BaseResponse<>(ResponseOfReadBoardList.of(boardList));
    }
}
