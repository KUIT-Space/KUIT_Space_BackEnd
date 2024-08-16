package space.space_spring.dto.comment.request;

import lombok.*;
import space.space_spring.entity.Comment;
import space.space_spring.entity.Post;
import space.space_spring.entity.User;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {
    private String content;
    private boolean isReply;
    private long targetId;

    public Comment toEntity(User user, Post post) {
        return Comment.builder()
                .user(user)
                .post(post)
                .content(content)
                .isReply(isReply)
                .targetId(targetId)
                .build();
    }

}