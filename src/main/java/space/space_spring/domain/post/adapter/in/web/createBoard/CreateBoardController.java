package space.space_spring.domain.post.adapter.in.web.createBoard;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardCommand;
import space.space_spring.domain.post.application.port.in.createBoard.CreateBoardUseCase;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_BOARD_CREATE;
import static space.space_spring.global.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시판 관련 API")
public class CreateBoardController {

    private final CreateBoardUseCase createBoardUseCase;

    @Operation(summary = "게시판 생성", description = """
            
            관리자가 게시판을 생성합니다.
            
            """)
    @PostMapping("/space/{spaceId}/board/create")
    public BaseResponse<Long> createBoard(
            @PathVariable Long spaceId,
            @Validated @RequestBody RequestOfCreateBoard request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_BOARD_CREATE, getErrorMessage(bindingResult));
        }

        // Request DTO -> Command 변환
        CreateBoardCommand command = CreateBoardCommand.builder()
                .spaceId(spaceId)
                .discordId(request.getDiscordId())
                .boardType(BoardType.fromString(request.getBoardType()))
                .boardName(request.getBoardName())
                .webhookUrl(request.getWebhookUrl())
                .build();

        // Board 생성
        Long boardId = createBoardUseCase.createBoard(command);
        return new BaseResponse<>(boardId);
    }

}
