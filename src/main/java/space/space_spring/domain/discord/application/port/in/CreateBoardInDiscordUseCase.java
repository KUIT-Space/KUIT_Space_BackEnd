package space.space_spring.domain.discord.application.port.in;

public interface CreateBoardInDiscordUseCase {
    Long create(CreateBoardInDiscordCommand command);
}
