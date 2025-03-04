package space.space_spring.domain.post.adapter.out.persistence.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCommentCount {

    private Long postId;
    private Long commentCount;
}
