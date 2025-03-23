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

    private Long likeCount;

    private Boolean isLiked;

    private Boolean isActiveComment;

    // Querydsl에서 사용되는 생성자
    public CommentDetailView(String creatorName, String creatorProfileImageUrl,
                             Boolean isPostOwner, String content,
                             LocalDateTime createdAt, LocalDateTime lastModifiedAt,
                             Long likeCount, Boolean isLiked, Boolean isActiveComment) {
        this.creatorName = creatorName;
        this.creatorProfileImageUrl = creatorProfileImageUrl;
        this.isPostOwner = isPostOwner;
        this.content = content;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.isActiveComment = isActiveComment;
    }
}
