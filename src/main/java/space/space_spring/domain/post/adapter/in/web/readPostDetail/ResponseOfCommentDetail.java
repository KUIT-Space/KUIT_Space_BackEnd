package space.space_spring.domain.post.adapter.in.web.readPostDetail;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.application.port.in.readPostDetail.InfoOfCommentDetail;

@Getter
public class ResponseOfCommentDetail {

    private Long commentId;

    private String creatorName;

    private String creatorProfileImageUrl;

    private Boolean isPostOwner;

    private String content;         // 댓글 내용은 String (Content X)

    private String createdAt;

    private String lastModifiedAt;

    private int likeCount;

    private Boolean isLiked;

    private Boolean isActiveComment;

    @Builder
    private ResponseOfCommentDetail(Long commentId, String creatorName, String creatorProfileImageUrl, Boolean isPostOwner, String content, String createdAt,
                                    String lastModifiedAt, int likeCount, Boolean isLiked, Boolean isActiveComment) {
        this.commentId = commentId;
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

    public static ResponseOfCommentDetail of(InfoOfCommentDetail info) {
        return ResponseOfCommentDetail.builder()
                .commentId(info.getCommentId())
                .creatorName(info.getCreatorName())
                .creatorProfileImageUrl(info.getCreatorProfileImageUrl())
                .isPostOwner(info.getIsPostOwner())
                .content(info.getContent())
                .createdAt(info.getCreatedAt())
                .lastModifiedAt(info.getLastModifiedAt())
                .likeCount(info.getLikeCount())
                .isLiked(info.getIsLiked())
                .isActiveComment(info.getIsActiveComment())
                .build();
    }

}
