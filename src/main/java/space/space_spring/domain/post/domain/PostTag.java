package space.space_spring.domain.post.domain;

import lombok.Getter;

@Getter
public class PostTag {

    private Long id;

    private Long tagId;

    private Long postBaseId;

    private PostTag(Long id, Long tagId, Long postBaseId) {
        this.id = id;
        this.tagId = tagId;
        this.postBaseId = postBaseId;
    }

    public static PostTag of(Long id, Long tagId, Long postBaseId) {
        return new PostTag(id, tagId, postBaseId);
    }
}
