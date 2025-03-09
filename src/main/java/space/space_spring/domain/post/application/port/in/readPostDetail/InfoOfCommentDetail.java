package space.space_spring.domain.post.application.port.in.readPostDetail;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.domain.Content;
import space.space_spring.global.util.NaturalNumber;

@Getter
public class InfoOfCommentDetail {

    private String creatorName;

    private String creatorProfileImageUrl;

    private boolean isPostOwner;

    private Content content;

    private String createdAt;

    private NaturalNumber likeCount;

    private boolean isLiked;

    @Builder
    public InfoOfCommentDetail(String creatorName, String creatorProfileImageUrl, boolean isPostOwner, Content content, String createdAt, NaturalNumber likeCount, boolean isLiked) {
        this.creatorName = creatorName;
        this.creatorProfileImageUrl = creatorProfileImageUrl;
        this.isPostOwner = isPostOwner;
        this.content = content;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }

}
