package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class Post {

    private Long id;

    private Long postBaseId;

    private String title;

    private Post(Long id, Long postBaseId, String title) {
        this.id = id;
        this.postBaseId = postBaseId;
        this.title = title;
    }

    public static Post of(Long id, Long postBaseId, String title) {
        return new Post(id, postBaseId, title);
    }

    public static Post withoutId(Long postBaseId, String title) {
        return new Post(null, postBaseId, title);
    }

}
