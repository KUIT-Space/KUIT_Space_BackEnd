package space.space_spring.domain.post.application.port.in.readPostDetail;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.domain.Content;
import space.space_spring.global.util.NaturalNumber;

import java.util.List;

@Getter
@Builder
public class ResultOfReadPostDetail {

    private String creatorName;

    private String creatorProfileImageUrl;

    private String createdAt;

    private String lastModifiedAt;

    private String title;

    private String content;

    private List<String> attachmentUrls;

    private int likeCount;

    private Boolean isLiked;

    private List<InfoOfCommentDetail> infoOfCommentDetails;
}
