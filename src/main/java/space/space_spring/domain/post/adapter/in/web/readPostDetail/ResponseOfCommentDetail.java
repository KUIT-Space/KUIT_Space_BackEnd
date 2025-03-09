package space.space_spring.domain.post.adapter.in.web.readPostDetail;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.application.port.in.readPostDetail.InfoOfCommentDetail;

@Getter
public class ResponseOfCommentDetail {

    private String creatorName;

    private String creatorProfileImageUrl;

    private boolean isPostOwner;

    private String content;

    private String createdAt;

    private int likeCount;

    private boolean isLiked;

    @Builder
    private ResponseOfCommentDetail(String creatorName, String creatorProfileImageUrl, boolean isPostOwner, String content, String createdAt, int likeCount, boolean isLiked) {
        this.creatorName = creatorName;
        this.creatorProfileImageUrl = creatorProfileImageUrl;
        this.isPostOwner = isPostOwner;
        this.content = content;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }

    public static ResponseOfCommentDetail of(InfoOfCommentDetail info) {
        return ResponseOfCommentDetail.builder()
                .creatorName(info.getCreatorName())
                .creatorProfileImageUrl(info.getCreatorProfileImageUrl())
                .isPostOwner(info.isPostOwner())
                .content(info.getContent().getValue())
                .createdAt(info.getCreatedAt())
                .likeCount(info.getLikeCount().getNumber())
                .isLiked(info.isLiked())
                .build();
    }

}
