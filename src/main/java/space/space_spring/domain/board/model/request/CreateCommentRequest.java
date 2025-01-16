package space.space_spring.domain.board.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import space.space_spring.domain.board.model.entity.Comment;
import space.space_spring.domain.board.model.entity.Post;
import space.space_spring.domain.user.model.entity.User;


public class CreateCommentRequest {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank
        private String content;
        private Long targetId = null;

        @Builder
        public Comment toEntity(User user, Post post) {
            return Comment.builder()
                    .user(user)
                    .post(post)
                    .content(content)
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