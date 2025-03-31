package space.space_spring.domain.post.application.port.out.comment;

import com.amazonaws.services.ec2.model.PriceSchedule;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDetailView {

    private Long commentId;

    private Long creatorId;     // 댓글 작성자의 spaceMemberId

    private String creatorName;

    private String creatorProfileImageUrl;

    private Boolean isPostOwner;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private Long likeCount;

    private Boolean isLiked;

    private Boolean isActiveComment;

    private Boolean isAnonymousComment;

    // Querydsl에서 사용되는 생성자
    public CommentDetailView(Long commentId, Long creatorId, String creatorName, String creatorProfileImageUrl,
                             Boolean isPostOwner, String content,
                             LocalDateTime createdAt, LocalDateTime lastModifiedAt,
                             Long likeCount, Boolean isLiked, Boolean isActiveComment, Boolean isAnonymousComment) {
        this.commentId = commentId;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.creatorProfileImageUrl = creatorProfileImageUrl;
        this.isPostOwner = isPostOwner;
        this.content = content;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.isActiveComment = isActiveComment;
        this.isAnonymousComment = isAnonymousComment;
    }
}
