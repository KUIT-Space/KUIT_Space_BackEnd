package space.space_spring.domain.post.domain;

import lombok.Getter;
import org.w3c.dom.Text;

@Getter
public class Comment {

    private Long id;

    private Long postId;

    private Long questionId;

    private Comment(Long id, Long postId, Long questionId) {
        this.id = id;
        this.postId = postId;
        this.questionId = questionId;
    }

    public static Comment createForPost(Long id, Long postId) {
        return new Comment(id, postId, null);
    }

    public static Comment withoutIdForPost(Long postId) {
        return new Comment(null, postId, null);
    }

    public static Comment createForQuestion(Long id, Long questionId) {
        return new Comment(id, null, questionId);
    }

    public static Comment withoutIdForQuestion(Long questionId) {
        return new Comment(null, null, questionId);
    }
}
