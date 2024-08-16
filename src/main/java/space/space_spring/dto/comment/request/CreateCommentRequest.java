package space.space_spring.dto.comment.request;

import com.mongodb.lang.Nullable;
import lombok.*;
import space.space_spring.entity.Comment;
import space.space_spring.entity.Post;
import space.space_spring.entity.User;


public class CreateCommentRequest {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String content;
        private boolean isReply;
        @Nullable
        private long targetId;

        @Builder
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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long CommentId;

        public static Response of(Long commentId) {
            return new Response(commentId);
        }
    }
}