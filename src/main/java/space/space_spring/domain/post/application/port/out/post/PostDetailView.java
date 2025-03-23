package space.space_spring.domain.post.application.port.out.post;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostDetailView {

    private String creatorName;

    private String creatorProfileImageUrl;

    private LocalDateTime createdAt;

    private LocalDateTime lastModifiedAt;

    private String title;

    private String content;

    private List<String> attachmentUrls;

    private Long likeCount;

    private Boolean isLiked;

    // Querydsl에서 사용할 생성자
    public PostDetailView(String creatorName, String creatorProfileImageUrl,
                          LocalDateTime createdAt, LocalDateTime lastModifiedAt,
                          String title, String content, List<String> attachmentUrls,
                          Long likeCount, Boolean isLiked) {
        this.creatorName = creatorName;
        this.creatorProfileImageUrl = creatorProfileImageUrl;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.title = title;
        this.content = content;
        this.attachmentUrls = attachmentUrls;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }
}
