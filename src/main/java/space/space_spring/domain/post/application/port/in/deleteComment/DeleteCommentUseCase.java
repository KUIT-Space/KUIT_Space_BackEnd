package space.space_spring.domain.post.application.port.in.deleteComment;

public interface DeleteCommentUseCase {

    void deleteCommentFromWeb(DeleteCommentCommand command);

    void deleteCommentFromDiscord(Long commentId);
}
