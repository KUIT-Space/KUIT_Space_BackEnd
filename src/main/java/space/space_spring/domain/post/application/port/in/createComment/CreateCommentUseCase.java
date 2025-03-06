package space.space_spring.domain.post.application.port.in.createComment;

public interface CreateCommentUseCase {

    Long createCommentFromWeb(CreateCommentFromWebCommand command);

    Long createCommentFromDiscord(CreateCommentFromDiscordCommand command);
}
