package space.space_spring.domain.post.adapter.in.web.createComment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.createComment.CreateCommentFromWebCommand;
import space.space_spring.domain.post.application.port.in.createComment.CreateCommentUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_COMMENT_CREATE;
import static space.space_spring.global.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 관련 API")
public class CreateCommentController {

    private final CreateCommentUseCase createCommentUseCase;

    @Operation(summary = "댓글 생성", description = """
            
            스페이스 멤버가 게시글(= 일반 게시글, 질문, Tip) 에 댓글을 생성합니다.
            
            """)
    @PostMapping("/space/{spaceId}/board/{boardId}/post/{postId}/comment")
    public BaseResponse<ResponseOfCreateComment> createComment(@JwtLoginAuth Long spaceMemberId,
                                                               @PathVariable("spaceId") Long spaceId, @PathVariable("boardId") Long boardId, @PathVariable("postId") Long postId,
                                                               @Validated @RequestBody RequestOfCreateComment request,
                                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_COMMENT_CREATE, getErrorMessage(bindingResult));
        }

        CreateCommentFromWebCommand command = CreateCommentFromWebCommand.builder()
                .spaceId(spaceId)
                .boardId(boardId)
                .postId(postId)
                .commentCreatorId(spaceMemberId)
                .content(request.getContent())
                .isAnonymous(request.isAnonymous())
                .build();

        return new BaseResponse<>(ResponseOfCreateComment.of(createCommentUseCase.createCommentFromWeb(command)));
    }
}
