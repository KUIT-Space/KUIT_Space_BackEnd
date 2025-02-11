package space.space_spring.domain.post.adapter.in.web;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.CreateBoard.CreateBoardCommand;
import space.space_spring.domain.post.application.port.in.CreateBoard.CreateBoardUseCase;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_BOARD_CREATE;
import static space.space_spring.global.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final CreateBoardUseCase createBoardUseCase;

    @PostMapping("/board/create")
    public BaseResponse<Long> createBoard(@Validated @RequestBody RequestOfCreateBoard request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_BOARD_CREATE, getErrorMessage(bindingResult));
        }

        // Request DTO -> Command 변환
        CreateBoardCommand command = CreateBoardCommand.builder()
                .spaceId(request.getSpaceId())
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
