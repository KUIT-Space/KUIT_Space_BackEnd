package space.space_spring.domain.post.adapter.in.web.readPostDetail;

import lombok.Builder;
import lombok.Getter;
import space.space_spring.domain.post.application.port.in.readPostDetail.InfoOfCommentDetail;

@Getter
public class ResponseOfCommentDetail {

    private String creatorName;

    private String creatorProfileImageUrl;

    private boolean isPostOwner;

    private String content;         // 댓글 내용은 String (Content X)

    private String createdAt;

    private String lastModifiedAt;

    private int likeCount;

    private boolean isLiked;

    @Builder
    private ResponseOfCommentDetail(String creatorName, String creatorProfileImageUrl, boolean isPostOwner, String content, String createdAt,
                                    String lastModifiedAt, int likeCount, boolean isLiked) {
        this.creatorName = creatorName;
        this.creatorProfileImageUrl = creatorProfileImageUrl;
        this.isPostOwner = isPostOwner;
        this.content = content;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }

    public static ResponseOfCommentDetail of(InfoOfCommentDetail info) {
        return ResponseOfCommentDetail.builder()
                .creatorName(info.getCreatorName())
                .creatorProfileImageUrl(info.getCreatorProfileImageUrl())
                .isPostOwner(info.isPostOwner())
                .content(info.getContent())
                .createdAt(info.getCreatedAt())
                .lastModifiedAt(info.getLastModifiedAt())
                .likeCount(info.getLikeCount().getNumber())
                .isLiked(info.isLiked())
                .build();
    }

}
