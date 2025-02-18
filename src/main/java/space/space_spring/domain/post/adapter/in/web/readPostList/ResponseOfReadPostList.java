package space.space_spring.domain.post.adapter.in.web.readPostList;

import lombok.Getter;
import space.space_spring.domain.post.application.port.in.readPostList.SummaryOfPost;

@Getter
public class ResponseOfReadPostList {

    private Long postId;

    private String title;

    private String content;

    private int likeCount;

    private int commentCount;

//    BaseDomainEntity 추가되면 할 것
//    private String createdAt;

    private String createdBy;

    private String postImageUrl;

    private ResponseOfReadPostList(Long postId, String title, String content, int likeCount, int commentCount, String createdBy, String postImageUrl) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
//        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.postImageUrl = postImageUrl;
    }

    public static ResponseOfReadPostList of(SummaryOfPost summaryOfPost) {
        return new ResponseOfReadPostList(
                summaryOfPost.getPost().getId(),
                summaryOfPost.getPost().getTitle(),
                summaryOfPost.getPost().getContent().getValue(),
                summaryOfPost.getLikeCount(),
                summaryOfPost.getCommentCount(),
                summaryOfPost.getCreatedBy().getNickname(),
                summaryOfPost.getPostImageUrl()
        );
    }
}
