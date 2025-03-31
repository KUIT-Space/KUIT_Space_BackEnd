package space.space_spring.domain.post.application.service.strategy;

import space.space_spring.domain.post.application.port.out.comment.CommentDetailView;

import java.util.HashMap;
import java.util.Map;

public class AnonymousCommentProcessingStrategy implements CommentDetailProcessingStrategy {

    private static final String POST_CREATOR_NICKNAME = "게시글 작성자";
    private static final String ANONYMOUS_COMMENT_CREATOR_NICKNAME = "익명 스페이서";
    private final Map<Long, String> anonymousNicknameMap = new HashMap<>();
    private int anonymousCount = 1;

    @Override
    public boolean supports(CommentDetailView comment) {
        return comment.getIsActiveComment() && comment.getIsAnonymousComment();
    }

    @Override
    public CommentDetailView process(CommentDetailView comment) {
        if (comment.getIsPostOwner()) {
            return CommentDetailView.builder()
                    .commentId(comment.getCommentId())
                    .creatorId(comment.getCreatorId())
                    .creatorName(POST_CREATOR_NICKNAME)
                    .creatorProfileImageUrl(null)
                    .isPostOwner(comment.getIsPostOwner())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .lastModifiedAt(comment.getLastModifiedAt())
                    .likeCount(comment.getLikeCount())
                    .isLiked(comment.getIsLiked())
                    .isActiveComment(comment.getIsActiveComment())
                    .isAnonymousComment(comment.getIsAnonymousComment())
                    .build();
        }

        Long creatorId = comment.getCreatorId();
        String nickname = anonymousNicknameMap.computeIfAbsent(creatorId,
                id -> ANONYMOUS_COMMENT_CREATOR_NICKNAME + " " + anonymousCount++);
        return CommentDetailView.builder()
                .commentId(comment.getCommentId())
                .creatorId(comment.getCreatorId())
                .creatorName(nickname)
                .creatorProfileImageUrl(null)
                .isPostOwner(comment.getIsPostOwner())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .lastModifiedAt(comment.getLastModifiedAt())
                .likeCount(comment.getLikeCount())
                .isLiked(comment.getIsLiked())
                .isActiveComment(comment.getIsActiveComment())
                .isAnonymousComment(comment.getIsAnonymousComment())
                .build();
    }
}
