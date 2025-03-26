package space.space_spring.domain.post.application.port.out.comment;

import java.util.List;

public interface CommentCreatorQueryPort {

    List<AnonymousCommentCreatorView> loadAnonymousCommentCreators(Long postId);
}
