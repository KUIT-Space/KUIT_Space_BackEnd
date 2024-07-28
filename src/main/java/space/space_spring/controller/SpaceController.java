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
import space.space_spring.service.S3Uploader;
import space.space_spring.service.SpaceService;
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.io.IOException;
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
    private final S3Uploader s3Uploader;
    private final String spaceImgDirName = "spaceImg";

    @PostMapping("/create")
    public BaseResponse<PostSpaceCreateResponse> createSpace(@JwtLoginAuth Long userId, @Validated @ModelAttribute PostSpaceCreateRequest postSpaceCreateRequest, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new SpaceException(INVALID_SPACE_CREATE, getErrorMessage(bindingResult));
        }

        // TODO 1. 스페이스 썸네일을 s3에 upload
        String spaceImgUrl = s3Uploader.upload(postSpaceCreateRequest.getSpaceProfileImg(), spaceImgDirName);

        // TODO 2. s3에 저장하고 받은 이미지 url 정보와 spaceName 정보로 space create 작업 수행
        return new BaseResponse<>(spaceService.createSpace(userId, postSpaceCreateRequest.getSpaceName(), spaceImgUrl));
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
