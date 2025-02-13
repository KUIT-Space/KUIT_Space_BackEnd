package space.space_spring.domain.post.adapter.in.web.createPost;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostCommand;
import space.space_spring.domain.post.application.port.in.createPost.CreatePostUseCase;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.global.exception.CustomException;

import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.INVALID_POST_CREATE;
import static space.space_spring.global.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
public class createPostController {

    private final CreatePostUseCase createPostUseCase;

    @PostMapping("/board/{boardId}/post")
    public BaseResponse<Long> createPost(
            @JwtLoginAuth Long id,
            @RequestParam Long boardId,
            @Validated @RequestBody RequestOfCreatePost request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_POST_CREATE, getErrorMessage(bindingResult));
        }

        CreatePostCommand command = CreatePostCommand.create(id, boardId, request.getTitle(), request.getContent(), request.getAttachments());
        return new BaseResponse<>(createPostUseCase.createPost(command));



    }


}
