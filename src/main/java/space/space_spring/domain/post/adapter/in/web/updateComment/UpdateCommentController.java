package space.space_spring.domain.post.adapter.in.web.updateComment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.updateComment.UpdateCommentCommand;
import space.space_spring.domain.post.application.port.in.updateComment.UpdateCommentUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_COMMENT_UPDATE;
import static space.space_spring.global.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 관련 API")
public class UpdateCommentController {

    private final UpdateCommentUseCase updateCommentUseCase;

    @Operation(summary = "댓글 수정", description = """
            
            스페이스 멤버가 자신이 생성한 댓글을 수정합니다.
            
            """)
    @PutMapping("/space/{spaceId}/board/{boardId}/post/{postId}/comment/{commentId}")
    public BaseResponse<SuccessResponse> updateComment(@JwtLoginAuth Long spaceMemberId,
                                                       @PathVariable("spaceId") Long spaceId, @PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId, @PathVariable("commentId") Long commentId,
                                                       @Validated @RequestBody RequestOfUpdateComment request, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_COMMENT_UPDATE, getErrorMessage(bindingResult));
        }

        UpdateCommentCommand command = UpdateCommentCommand.builder()
                .commentId(commentId)
                .spaceId(spaceId)
                .boardId(boardId)
                .postId(postId)
                .commentCreatorId(spaceMemberId)
                .content(request.getContent())
                .build();

        updateCommentUseCase.updateComment(command);

        return new BaseResponse<>(new SuccessResponse(true));
    }
}
