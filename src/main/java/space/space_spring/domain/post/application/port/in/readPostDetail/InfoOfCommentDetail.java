package space.space_spring.domain.post.application.port.in.readPostDetail;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.global.util.NaturalNumber;

@Getter
@Builder
public class InfoOfCommentDetail {

    private String creatorName;

    private String creatorProfileImageUrl;

    private Boolean isPostOwner;

    private String content;

    private String createdAt;

    private String lastModifiedAt;

    private int likeCount;

    private Boolean isLiked;

    private Boolean isActiveComment;
}
