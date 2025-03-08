package space.space_spring.domain.post.adapter.in.web.createPost;

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
public class CreatePostController {

    private final CreatePostUseCase createPostUseCase;

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
