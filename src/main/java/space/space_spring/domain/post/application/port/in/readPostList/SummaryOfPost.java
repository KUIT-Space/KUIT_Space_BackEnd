package space.space_spring.domain.post.application.port.in.readPostList;

import lombok.Getter;
import space.space_spring.domain.post.domain.Post;
import space.space_spring.domain.spaceMember.domian.SpaceMember;

@Getter
public class SummaryOfPost {

    private Post post;
    private SpaceMember createdBy;
    private int likeCount;
    private int commentCount;
    private String postImageUrl;

    private SummaryOfPost(Post post, SpaceMember createdBy, int likeCount, int commentCount, String postImageUrl) {
        this.post = post;
        this.createdBy = createdBy;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.postImageUrl = postImageUrl;
    }

    public static SummaryOfPost of(Post post, SpaceMember createdBy, int likeCount, int commentCount, String postImageUrl) {
        return new SummaryOfPost(post, createdBy, likeCount, commentCount, postImageUrl);
    }
}
