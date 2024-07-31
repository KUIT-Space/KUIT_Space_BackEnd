package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.argument_resolver.jwtLogin.JwtLoginAuth;
import space.space_spring.dto.space.GetUserInfoBySpaceResponse;
import space.space_spring.dto.space.PostSpaceCreateRequest;
import space.space_spring.dto.space.PostSpaceCreateResponse;

import space.space_spring.entity.UserSpace;
import space.space_spring.exception.MultipartFileException;
import space.space_spring.exception.SpaceException;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.S3Uploader;
import space.space_spring.service.SpaceService;
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.io.IOException;
import java.util.Optional;

import static space.space_spring.response.status.BaseExceptionResponseStatus.INVALID_SPACE_CREATE;
import static space.space_spring.response.status.BaseExceptionResponseStatus.IS_NOT_IMAGE_FILE;
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

    @PostMapping("")
    public BaseResponse<PostSpaceCreateResponse> createSpace(@JwtLoginAuth Long userId, @Validated @ModelAttribute PostSpaceCreateRequest postSpaceCreateRequest, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new SpaceException(INVALID_SPACE_CREATE, getErrorMessage(bindingResult));
        }

        // TODO 1. 스페이스 썸네일을 s3에 upload
        String spaceImgUrl = processSpaceImage(postSpaceCreateRequest.getSpaceProfileImg());

        // TODO 2. s3에 저장하고 받은 이미지 url 정보와 spaceName 정보로 space create 작업 수행
        return new BaseResponse<>(spaceService.createSpace(userId, postSpaceCreateRequest.getSpaceName(), spaceImgUrl));
    }

    private String processSpaceImage(MultipartFile spaceProfileImg) throws IOException {
        if (spaceProfileImg == null) {
            return null;
        }
        validateImageFile(spaceProfileImg);
        return s3Uploader.upload(spaceProfileImg, spaceImgDirName);

    }

    private void validateImageFile(MultipartFile spaceProfileImg) {
        if (!s3Uploader.isFileImage(spaceProfileImg)) {
            throw new MultipartFileException(IS_NOT_IMAGE_FILE);
        }
    }

    /**
     * 스페이스의 모든 유저 정보 조회
     */
    @GetMapping("/{spaceId}/all-member")
    public BaseResponse<GetUserInfoBySpaceResponse> getAllUserInfoBySpace(@PathVariable Long spaceId) {

        return new BaseResponse<>(spaceService.findUserInfoBySpace(spaceId));
    }

}
