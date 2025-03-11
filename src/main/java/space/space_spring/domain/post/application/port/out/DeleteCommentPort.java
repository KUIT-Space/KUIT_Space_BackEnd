package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Comment;

import java.util.List;

public interface DeleteCommentPort {

    void deleteComment(Long commentId);

    void deleteAllComments(List<Comment> comments);
}
