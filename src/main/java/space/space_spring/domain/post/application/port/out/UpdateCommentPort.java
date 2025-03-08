package space.space_spring.domain.post.application.port.out;

import space.space_spring.domain.post.domain.Comment;

public interface UpdateCommentPort {

    void updateComment(Comment comment);
}
