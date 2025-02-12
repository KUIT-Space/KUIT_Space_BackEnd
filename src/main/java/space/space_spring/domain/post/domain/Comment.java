package space.space_spring.domain.post.domain;

import lombok.Getter;
import org.w3c.dom.Text;

@Getter
public class Comment {

    private Long id;

    private Long postBaseId;

    private Long postId;

    private Long questionId;

    private Comment(Long id, Long postBaseId, Long postId, Long questionId) {
        this.id = id;
        this.postBaseId = postBaseId;
        this.postId = postId;
        this.questionId = questionId;
    }

    public static Comment of(Long id, Long postBaseId, Long postId, Long questionId) {
        return new Comment(id, postBaseId, postId, questionId);
    }

    public static Comment withoutId(Long postBaseId, Long postId, Long questionId) {
        return new Comment(null, postBaseId, postId, questionId);
    }
}
