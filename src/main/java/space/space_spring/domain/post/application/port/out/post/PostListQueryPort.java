package space.space_spring.domain.post.application.port.out.post;

import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import space.space_spring.domain.post.application.port.in.readPostList.PostSummary;

public interface PostListQueryPort {

    Page<PostSummary> queryPostSummaries(
            Long spaceMemberId,
            Long boardId,
            @Nullable Long tagId,
            Pageable pageable);
}
