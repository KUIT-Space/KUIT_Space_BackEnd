package space.space_spring.domain.discord.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.discord.application.port.in.CreateBoardInDiscordCommand;
import space.space_spring.domain.discord.application.port.in.CreateBoardInDiscordUseCase;
import space.space_spring.domain.post.application.port.in.CreateBoard.CreateBoardCommand;
import space.space_spring.domain.post.application.port.in.CreateBoard.CreateBoardUseCase;
import space.space_spring.domain.post.domain.BoardType;
import space.space_spring.domain.space.application.port.out.LoadSpacePort;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.SPACE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CreateBoardInDiscordService implements CreateBoardInDiscordUseCase {
    private final LoadSpacePort loadSpacePort;
    private final CreateBoardUseCase createBoardUseCase;
    @Override
    public Long create(CreateBoardInDiscordCommand command){
        Long spaceId = loadSpacePort.loadSpaceByDiscordId(command.getGuildDiscordId()).orElseThrow(()->new CustomException(SPACE_NOT_FOUND)).getId();

        return createBoardUseCase.createBoard(mapToCreateBoardCommand(spaceId,command));
    }

    private CreateBoardCommand mapToCreateBoardCommand(Long spaceId, CreateBoardInDiscordCommand command){
        return CreateBoardCommand.builder()
                .boardName(command.getName())
                .discordId(command.getChannelDiscordId())
                .boardType(BoardType.fromString(command.getBoardType()))
                .spaceId(spaceId)
                .webhookUrl(command.getWebHookUrl())
                .build();
    }
}
