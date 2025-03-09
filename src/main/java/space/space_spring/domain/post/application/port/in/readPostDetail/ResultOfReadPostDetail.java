package space.space_spring.domain.post.application.port.in.readPostDetail;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.domain.Content;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;

@Getter
public class ResultOfReadPostDetail {

    private String creatorName;

    private String creatorProfileImageUrl;

    private String createdAt;

    private String title;

    private Content content;

    private List<String> attachmentUrls;

    private NaturalNumber likeCount;

    private boolean isLiked;

    private List<InfoOfCommentDetail> infoOfCommentDetails;

    @Builder
    public ResultOfReadPostDetail(String creatorName, String creatorProfileImageUrl, String createdAt, String title, Content content,
                                  List<String> attachmentUrls, NaturalNumber likeCount, boolean isLiked, List<InfoOfCommentDetail> infoOfCommentDetails) {
        this.creatorName = creatorName;
        this.creatorProfileImageUrl = creatorProfileImageUrl;
        this.createdAt = createdAt;
        this.title = title;
        this.content = content;
        this.attachmentUrls = attachmentUrls;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.infoOfCommentDetails = infoOfCommentDetails;
    }
}
