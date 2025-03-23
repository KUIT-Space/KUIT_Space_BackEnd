package space.space_spring.domain.post.application.port.out.comment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDetailView {

    private String creatorName;

    private String creatorProfileImageUrl;

    private Boolean isPostOwner;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private int likeCount;

    private Boolean isLiked;

    private Boolean isActiveComment;
}
