package space.space_spring.domain.discord.application.port.in.discord;

public interface InputMessageFromDiscordUseCase {
    void putPost(MessageInputFromDiscordCommand command);
    void putComment(CommentInputFromDiscordCommand command,Long originPostDiscordId);
}
