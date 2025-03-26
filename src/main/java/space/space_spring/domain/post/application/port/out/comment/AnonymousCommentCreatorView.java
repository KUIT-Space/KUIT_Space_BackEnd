package space.space_spring.domain.post.application.port.out.comment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnonymousCommentCreatorView {

    private Long creatorId;

    private Boolean isPostOwner;

    private Boolean isActiveComment;

    // QueryDsl에서 사용되는 생성자
    public AnonymousCommentCreatorView(Long creatorId, Boolean isPostOwner, Boolean isActiveComment) {
        this.creatorId = creatorId;
        this.isPostOwner = isPostOwner;
        this.isActiveComment = isActiveComment;
    }
}
