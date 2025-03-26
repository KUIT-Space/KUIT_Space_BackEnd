package space.space_spring.domain.post.adapter.in.web.readPostDetail;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.application.port.in.readPostDetail.ResultOfReadPostDetail;

import java.util.List;

@Getter
public class ResponseOfReadPostDetail {

    private String creatorName;

    private String creatorProfileImageUrl;

    private String createdAt;

    private String lastModifiedAt;

    private String title;

    private String content;

    private List<String> attachmentUrls;

    private int likeCount;

    private Boolean isLiked;

    private List<ResponseOfCommentDetail> responseOfCommentDetails;

    @Builder
    private ResponseOfReadPostDetail(String creatorName, String creatorProfileImageUrl, String createdAt, String lastModifiedAt,
                                     String title, String content, List<String> attachmentUrls, int likeCount, Boolean isLiked,
                                     List<ResponseOfCommentDetail> responseOfCommentDetails) {
        this.creatorName = creatorName;
        this.creatorProfileImageUrl = creatorProfileImageUrl;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.title = title;
        this.content = content;
        this.attachmentUrls = attachmentUrls;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.responseOfCommentDetails = responseOfCommentDetails;
    }

    public static ResponseOfReadPostDetail of(ResultOfReadPostDetail result) {
        return ResponseOfReadPostDetail.builder()
                .creatorName(result.getCreatorName())
                .creatorProfileImageUrl(result.getCreatorProfileImageUrl())
                .createdAt(result.getCreatedAt())
                .lastModifiedAt(result.getLastModifiedAt())
                .title(result.getTitle())
                .content(result.getContent())
                .attachmentUrls(result.getAttachmentUrls())
                .likeCount(result.getLikeCount())
                .isLiked(result.getIsLiked())
                .responseOfCommentDetails(
                        result.getInfoOfCommentDetails().stream()
                                .map(ResponseOfCommentDetail::of)
                                .toList())
                .build();
    }
}
