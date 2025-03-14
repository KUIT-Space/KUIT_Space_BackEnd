package space.space_spring.domain.post.application.port.in.readPostDetail;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.global.util.NaturalNumber;

@Getter
public class InfoOfCommentDetail {

    private String creatorName;

    private String creatorProfileImageUrl;

    private boolean isPostOwner;

    private String content;

    private String createdAt;

    private String lastModifiedAt;

    private NaturalNumber likeCount;

    private boolean isLiked;

    private boolean isActiveComment;

    @Builder
    public InfoOfCommentDetail(String creatorName, String creatorProfileImageUrl, boolean isPostOwner, String content, String createdAt, String lastModifiedAt, NaturalNumber likeCount, boolean isLiked, boolean isActiveComment) {
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
