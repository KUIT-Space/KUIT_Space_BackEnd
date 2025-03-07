package space.space_spring.domain.post.adapter.in.web.createPost;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_POST_CREATE;
import static space.space_spring.global.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post", description = "게시글 관련 API")
public class CreatePostController {

    private final CreatePostUseCase createPostUseCase;

    @Operation(summary = "게시글 생성", description = """
            
            스페이스 멤버가 게시글(=일반 게시글, 질문, Tip)을 생성합니다.
            
            """)
    @PostMapping("/space/{spaceId}/board/{boardId}/create")
    public BaseResponse<Long> createPost(
            @JwtLoginAuth Long spaceMemberId,
            @PathVariable Long spaceId,
            @PathVariable Long boardId,
            @Validated @RequestBody RequestOfCreatePost request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_POST_CREATE, getErrorMessage(bindingResult));
        }

        CreatePostCommand command = CreatePostCommand.builder()
                .postCreatorId(spaceMemberId)
                .boardId(boardId)
                .title(request.getTitle())
                .content(request.getContent())
                .attachments(request.getAttachments())
                .isAnonymous(request.getIsAnonymous())
                .build();

        return new BaseResponse<>(createPostUseCase.createPostFromWeb(spaceMemberId, spaceId, command));

    }
}
