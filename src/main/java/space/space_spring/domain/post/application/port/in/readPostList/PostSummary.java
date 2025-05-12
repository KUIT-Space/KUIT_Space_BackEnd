package space.space_spring.domain.post.application.port.in.readPostList;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostSummary {

    private Long postId;

    private String title;

    private String content;

    private int likeCount;

    private int commentCount;

    private LocalDateTime createdAt;

    private String creatorNickname;

    private String creatorProfileImageUrl;

    private String postImageUrl;

    private Boolean isPostOwner;

    public PostSummary(Long PostId, String title, String content, int likeCount, int commentCount,
                       LocalDateTime createdAt, String creatorNickname, String creatorProfileImageUrl, String postImageUrl, Boolean isPostOwner) {
        this.postId = PostId;
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.creatorNickname = creatorNickname;
        this.creatorProfileImageUrl = creatorProfileImageUrl;
        this.postImageUrl = postImageUrl;
        this.isPostOwner = isPostOwner;
    }
}
