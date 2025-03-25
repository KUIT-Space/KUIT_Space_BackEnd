package space.space_spring.domain.post.application.service.strategy;

import space.space_spring.domain.post.application.port.out.comment.CommentDetailView;

public class NormalCommentProcessingStrategy implements CommentDetailProcessingStrategy {

    @Override
    public boolean supports(CommentDetailView comment) {
        return comment.getIsActiveComment() && !comment.getIsAnonymousComment();
    }

    @Override
    public CommentDetailView process(CommentDetailView comment) {
        return comment;
    }
}
