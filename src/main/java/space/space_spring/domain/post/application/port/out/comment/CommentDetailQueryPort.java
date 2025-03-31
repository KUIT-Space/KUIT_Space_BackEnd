package space.space_spring.domain.post.application.port.out.comment;

import java.util.List;

public interface CommentDetailQueryPort {

    List<CommentDetailView> loadCommentDetail(Long postId, Long spaceMemberId);
}
