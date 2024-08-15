package space.space_spring.dto.comment.response;

import lombok.*;
import space.space_spring.entity.PostComment;
import space.space_spring.entity.UserSpace;
import space.space_spring.util.post.ConvertCreatedDate;

import java.util.List;

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
    private boolean isReply;
    private Long targetId;

    public static ReadCommentsResponse of(PostComment comment, UserSpace userSpace, boolean isLike, int commentCount) {
        return ReadCommentsResponse.builder()
                .commentId(comment.getCommentId())
                .userName(userSpace != null ? userSpace.getUserName() : null)
                .userProfileImg(userSpace != null ? userSpace.getUserProfileImg() : null)
                .content(comment.getContent())
                .time(ConvertCreatedDate.setCreatedDate(comment.getCreatedAt()))
                .commentCount(commentCount)
                .likeCount(comment.getLike())
                .isLike(isLike)
                .isReply(comment.isReply())
                .targetId(comment.getTargetId())
                .build();
    }

}
