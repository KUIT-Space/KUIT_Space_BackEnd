package space.space_spring.domain.discord.application.port.in.deleteComment;

public interface DeleteCommentInDiscordUseCase {

    void deleteCommentInDiscord(DeleteCommentInDiscordCommand command);
}
