package space.space_spring.domain.post.application.port.in.readPostList;

import lombok.Getter;
import space.space_spring.domain.post.domain.Content;

import java.time.LocalDateTime;

@Getter
public class PostSummary {

    private Long postId;

    private String title;

    private Content content;

    private int likeCount;

    private int commentCount;

    private LocalDateTime createdAt;

    private String creatorNickname;

    private String postImageUrl;

    private Boolean isPostOwner;

    private PostSummary(Long postId, String title, Content content, int likeCount, int commentCount,
                        LocalDateTime createdAt, String creatorNickname, String postImageUrl, Boolean isPostOwner) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.creatorNickname = creatorNickname;
        this.postImageUrl = postImageUrl;
        this.isPostOwner = isPostOwner;
    }

    public static PostSummary of(Long postId, String title, Content content, int likeCount, int commentCount,
                                 LocalDateTime createdAt, String creatorNickname, String postImageUrl, Boolean isPostOwner) {
        return new PostSummary(postId, title, content, likeCount, commentCount, createdAt, creatorNickname, postImageUrl, isPostOwner);
    }
}
