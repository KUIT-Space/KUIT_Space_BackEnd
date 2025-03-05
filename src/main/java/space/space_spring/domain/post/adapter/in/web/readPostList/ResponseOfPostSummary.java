package space.space_spring.domain.post.adapter.in.web.readPostList;

import lombok.Getter;
import space.space_spring.domain.post.application.port.in.readPostList.PostSummary;
import space.space_spring.global.util.post.ConvertCreatedDate;

@Getter
public class ResponseOfPostSummary {

    private Long postId;

    private String title;

    private String content;

    private int likeCount;

    private int commentCount;

    private String createdAt;

    private String creatorNickname;

    private String postImageUrl;

    private ResponseOfPostSummary(Long postId, String title, String content, int likeCount, int commentCount, String createdAt, String creatorNickname, String postImageUrl){
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.creatorNickname = creatorNickname;
        this.postImageUrl = postImageUrl;
    }

    public static ResponseOfPostSummary of(PostSummary summaryOfPost) {
        return new ResponseOfPostSummary(
                summaryOfPost.getPostId(),
                summaryOfPost.getTitle(),
                summaryOfPost.getContent().getValue(),
                summaryOfPost.getLikeCount(),
                summaryOfPost.getCommentCount(),
                ConvertCreatedDate.setCreatedDate(summaryOfPost.getCreatedAt()),
                summaryOfPost.getCreatorNickname(),
                summaryOfPost.getPostImageUrl()
        );
    }
}
