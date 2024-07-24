package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.space_spring.argument_resolver.jwtLogin.JwtLoginAuth;
import space.space_spring.dto.space.PostSpaceCreateRequest;
import space.space_spring.dto.space.PostSpaceCreateResponse;

import space.space_spring.entity.UserSpace;
import space.space_spring.exception.SpaceException;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.SpaceService;
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.util.Optional;

import static space.space_spring.response.status.BaseExceptionResponseStatus.INVALID_SPACE_CREATE;
import static space.space_spring.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space")
@Slf4j
public class SpaceController {

    private final SpaceService spaceService;
    private final UserSpaceUtils userSpaceUtils;

    @PostMapping("/create")
    public BaseResponse<PostSpaceCreateResponse> createSpace(@JwtLoginAuth Long userId, @Validated @RequestBody PostSpaceCreateRequest postSpaceCreateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new SpaceException(INVALID_SPACE_CREATE, getErrorMessage(bindingResult));
        }

        return new BaseResponse<>(spaceService.createSpace(userId, postSpaceCreateRequest));
    }

    /**
     *  테스트 용
     */
    @GetMapping("/{spaceId}")
    public BaseResponse<String> getSpaceHome(@JwtLoginAuth Long userId, @PathVariable Long spaceId) {
        Optional<UserSpace> userInSpace = userSpaceUtils.isUserInSpace(userId, spaceId);
        log.info("userInSpace.get().getUserName() = {}", userInSpace.get().getUserName());
        log.info("userInspace.get().getUserSpaceAuth() = {}", userInSpace.get().getUserSpaceAuth());

        return new BaseResponse<>("스페이스에 속한 유저만 걸러내는 작업 테스트 성공");
    }
}
