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

    private int likeCount;

    private Boolean isLiked;
}
