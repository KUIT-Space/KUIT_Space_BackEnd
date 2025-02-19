package space.space_spring.domain.post.application.port.in.readPostList;

import lombok.Getter;
import space.space_spring.domain.post.domain.Content;

@Getter
public class PostSummary {

    private Long postId;

    private String title;

    private Content content;

    private int likeCount;

    private int commentCount;

    private String createdBy;

    private String postImageUrl;

    private PostSummary(Long postId, String title, Content content, int likeCount, int commentCount, String createdBy, String postImageUrl) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdBy = createdBy;
        this.postImageUrl = postImageUrl;
    }

    public static PostSummary of(Long postId, String title, Content content, int likeCount, int commentCount, String createdBy, String postImageUrl) {
        return new PostSummary(postId, title, content, likeCount, commentCount, createdBy, postImageUrl);
    }
}
