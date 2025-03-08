package space.space_spring.domain.post.adapter.in.web.deleteComment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.deleteComment.DeleteCommentCommand;
import space.space_spring.domain.post.application.port.in.deleteComment.DeleteCommentUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;

@RestController
@RequiredArgsConstructor
public class DeleteCommentController {

    private final DeleteCommentUseCase deleteCommentUseCase;

    @DeleteMapping("/space/{spaceId}/board/{boardId}/post/{postId}/comment/{commentId}")
    public BaseResponse<SuccessResponse> deleteComment(@JwtLoginAuth Long spaceMemberId,
                                                       @PathVariable("spaceId") Long spaceId, @PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId) {

        deleteCommentUseCase.deleteComment(DeleteCommentCommand.of(spaceId, boardId, postId, commentId, spaceMemberId));

        return new BaseResponse<>(new SuccessResponse(true));
    }
}
