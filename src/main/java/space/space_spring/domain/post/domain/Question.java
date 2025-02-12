package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Question {

    private Long id;

    private Long postBaseId;

    private String title;

    private boolean isAnonymous;

    private Question(Long id, Long postBaseId, String title, boolean isAnonymous) {
        this.id = id;
        this.postBaseId = postBaseId;
        this.title = title;
        this.isAnonymous = isAnonymous;
    }

    public static Question of(Long id, Long postBaseId, String title, boolean isAnonymous) {
        return new Question(id, postBaseId,  title, isAnonymous);
    }

    public static Question withoutId(Long postBaseId, String title, boolean isAnonymous) {
        return new Question(null, postBaseId, title, isAnonymous);
    }

}
