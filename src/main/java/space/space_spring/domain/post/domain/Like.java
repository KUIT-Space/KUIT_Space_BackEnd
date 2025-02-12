package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Like {

    private Long id;

    private Long postBaseId;

    private Like(Long id, Long postBaseId) {
        this.id = id;
        this.postBaseId = postBaseId;
    }

    public static Like of(Long id, Long postBaseId) {
        return new Like(id, postBaseId);
    }

    public static Like withoutId(Long postBaseId) {
        return new Like(null, postBaseId);
    }
}
