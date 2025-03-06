package space.space_spring.domain.post.adapter.out.persistence.like;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostLikeCount {

    private Long postId;
    private Long likeCount;
}
