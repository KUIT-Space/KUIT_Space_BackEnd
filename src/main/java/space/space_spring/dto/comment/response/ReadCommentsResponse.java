package space.space_spring.dto.comment.response;

import lombok.*;
import space.space_spring.entity.Comment;
import space.space_spring.domain.userSpace.model.entity.UserSpace;
import space.space_spring.util.post.ConvertCreatedDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReadCommentsResponse {
    private Long commentId;

    private String userProfileImg;
    private String userName;

    private String content;
    private String time;

    private int commentCount;
    private int likeCount;
    private boolean isLike;
    private Long targetId;

    public static ReadCommentsResponse of(Comment comment, UserSpace userSpace, boolean isLike, int commentCount) {
        return ReadCommentsResponse.builder()
                .commentId(comment.getCommentId())
                .userName(userSpace != null ? userSpace.getUserName() : null)
                .userProfileImg(userSpace != null ? userSpace.getUserProfileImg() : null)
                .content(comment.getContent())
                .time(ConvertCreatedDate.setCreatedDate(comment.getCreatedAt()))
                .commentCount(commentCount)
                .likeCount(comment.getLikeCount())
                .isLike(isLike)
                .targetId(comment.getTargetId())
                .build();
    }

}
