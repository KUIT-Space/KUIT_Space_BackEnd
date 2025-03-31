package space.space_spring.domain.post.application.service.strategy;

import space.space_spring.domain.post.application.port.out.comment.CommentDetailView;

public class InactiveCommentProcessingStrategy implements CommentDetailProcessingStrategy {

    private static final String INACTIVE_COMMENT_CONTENT = "삭제된 댓글입니다.";

    @Override
    public boolean supports(CommentDetailView comment) {
        return !comment.getIsActiveComment();
    }

    @Override
    public CommentDetailView process(CommentDetailView comment) {
        return CommentDetailView.builder()
                .commentId(comment.getCommentId())
                .creatorId(comment.getCreatorId())
                .creatorName(null)
                .creatorProfileImageUrl(null)
                .isPostOwner(comment.getIsPostOwner())
                .content(INACTIVE_COMMENT_CONTENT)
                .createdAt(comment.getCreatedAt())
                .lastModifiedAt(comment.getLastModifiedAt())
                .likeCount(comment.getLikeCount())
                .isLiked(comment.getIsLiked())
                .isActiveComment(comment.getIsActiveComment())
                .isAnonymousComment(comment.getIsAnonymousComment())
                .build();
    }
}
