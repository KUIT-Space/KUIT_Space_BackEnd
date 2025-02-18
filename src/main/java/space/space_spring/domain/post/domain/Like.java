package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Like {

    private Long id;

    private Long postId;

    private Long questionId;

    private Like(Long id, Long postId, Long questionId) {
        this.id = id;
        this.postId = postId;
        this.questionId = questionId;
    }

    public static Like createForPost(Long id, Long postId) {
        return new Like(id, postId, null);
    }

    public static Like withoutIdForPost(Long postId) {
        return new Like(null, postId, null);
    }

    public static Like createForQuestion(Long id, Long questionId) {
        return new Like(id, null, questionId);
    }

    public static Like withoutIdForQuestion(Long questionId) {
        return new Like(null, null, questionId);
    }
}
