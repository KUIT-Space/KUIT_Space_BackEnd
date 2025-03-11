package space.space_spring.domain.post.adapter.in.web.readBoardList;

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
public class ReadBoardListController {

    private final ReadBoardListUseCase readBoardListUseCase;

    @GetMapping("/space/{spaceId}/board/list")
    public BaseResponse<ResponseOfReadBoardList> readBoardList(
            @JwtLoginAuth Long spaceMemberId,
            @PathVariable Long spaceId) {
        ReadBoardListCommand boardList = readBoardListUseCase.readBoardList(spaceMemberId, spaceId);
        return new BaseResponse<>(ResponseOfReadBoardList.of(boardList));
    }
}
