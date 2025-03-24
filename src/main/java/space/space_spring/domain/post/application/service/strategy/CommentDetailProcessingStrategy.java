package space.space_spring.domain.post.application.service.strategy;

import space.space_spring.domain.post.application.port.out.comment.CommentDetailView;

public interface CommentDetailProcessingStrategy {

    boolean supports(CommentDetailView comment);
    CommentDetailView process(CommentDetailView comment);
}
