package space.space_spring.domain.post.adapter.in.web.createComment;

import lombok.Getter;

@Getter
public class ResponseOfCreateComment {

    private Long commentId;

    private ResponseOfCreateComment(Long commentId) {
        this.commentId = commentId;
    }

    public static ResponseOfCreateComment of(Long commentId) {
        return new ResponseOfCreateComment(commentId);
    }
}
