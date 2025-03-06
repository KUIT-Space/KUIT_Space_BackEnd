package space.space_spring.domain.post.application.port.in.createComment;

public interface CreateCommentUseCase {

    Long createCommentFromWeb(CreateCommentCommand command);

    Long createCommentFromDiscord(CreateCommentCommand command, Long discordId);
}
