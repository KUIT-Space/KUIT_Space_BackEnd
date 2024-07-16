package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.argument_resolver.JwtPreAuth;
import space.space_spring.dto.space.PostSpaceCreateRequest;
import space.space_spring.dto.space.PostSpaceCreateResponse;

import space.space_spring.exception.SpaceException;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.SpaceService;

import static space.space_spring.response.status.BaseExceptionResponseStatus.INVALID_SPACE_CREATE;
import static space.space_spring.util.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space")
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping("/create")
    public BaseResponse<PostSpaceCreateResponse> createSpace(@JwtPreAuth Long userId, @Validated @RequestBody PostSpaceCreateRequest postSpaceCreateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new SpaceException(INVALID_SPACE_CREATE, getErrorMessage(bindingResult));
        }

        return new BaseResponse<>(spaceService.createSpace(userId, postSpaceCreateRequest));
    }

}
