package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Like {

    private Long id;

    private Long targetId; // post, question의 postBaseId

    private boolean isLiked;


    private Like(Long id, Long targetId, boolean isLiked) {
        this.id = id;
        this.targetId = targetId;
        this.isLiked = isLiked;
    }

    public static Like of(Long id, Long targetId, boolean isLiked) {
        return new Like(id, targetId, isLiked);
    }

    public static Like withoutId(Long targetId, boolean isLiked) {
        return new Like(null, targetId, isLiked);
    }
}
