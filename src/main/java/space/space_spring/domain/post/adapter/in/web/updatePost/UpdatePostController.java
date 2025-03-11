package space.space_spring.domain.post.adapter.in.web.updatePost;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.updatePost.UpdatePostCommand;
import space.space_spring.domain.post.application.port.in.updatePost.UpdatePostUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.common.response.SuccessResponse;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_POST_UPDATE;
import static space.space_spring.global.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 관련 API")
public class UpdatePostController {

    private final UpdatePostUseCase updatePostUseCase;

    @Operation(summary = "게시글 수정", description = """
            
            스페이스 멤버가 자신이 생성한 게시글을 수정합니다.
            
            """)

    @PutMapping(value = "/space/{spaceId}/board/{boardId}/post/{postId}", consumes = "multipart/form-data")
    public BaseResponse<SuccessResponse> updatePost(@JwtLoginAuth Long spaceMemberId,
                                                    @PathVariable Long spaceId,
                                                    @PathVariable Long boardId,
                                                    @PathVariable Long postId,
                                                    @Validated @ModelAttribute RequestOfUpdatePost request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_POST_UPDATE, getErrorMessage(bindingResult));
        }

        UpdatePostCommand command = UpdatePostCommand.builder()
                .spaceId(spaceId)
                .boardId(boardId)
                .postId(postId)
                .postCreatorId(spaceMemberId)
                .title(request.getTitle())
                .content(request.getContent())
                .attachments(request.getAttachments())
                .isAnonymous(request.getIsAnonymous())
                .build();

        updatePostUseCase.updatePostFromWeb(command);

        return new BaseResponse<>(new SuccessResponse(true));
    }
}
