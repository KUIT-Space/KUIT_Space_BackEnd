package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.argument_resolver.jwtLogin.JwtLoginAuth;
import space.space_spring.dto.space.GetSpaceJoinDto;
import space.space_spring.dto.space.response.GetUserInfoBySpaceResponse;
import space.space_spring.dto.space.request.PostSpaceCreateRequest;

import space.space_spring.dto.userSpace.GetUserProfileInSpaceDto;
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
    private final S3Uploader s3Uploader;
    private final String spaceImgDirName = "spaceImg";
    private final UserSpaceUtils userSpaceUtils;

    @PostMapping("")
    public BaseResponse<String> createSpace(@JwtLoginAuth Long userId, @Validated @ModelAttribute PostSpaceCreateRequest postSpaceCreateRequest, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new SpaceException(INVALID_SPACE_CREATE, getErrorMessage(bindingResult));
        }

        // TODO 1. 스페이스 썸네일을 s3에 upload
        String spaceImgUrl = processSpaceImage(postSpaceCreateRequest.getSpaceProfileImg());

        // TODO 2. s3에 저장하고 받은 이미지 url 정보와 spaceName 정보로 space create 작업 수행
        spaceService.createSpace(userId, postSpaceCreateRequest.getSpaceName(), spaceImgUrl);

        return new BaseResponse<>("스페이스 생성 성공");
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

    /**
     * 스페이스 가입 view를 위한 데이터 조회
     */
    @GetMapping("/{spaceId}/join")
    public BaseResponse<GetSpaceJoinDto.Response> getSpaceJoin(@JwtLoginAuth Long userId, @PathVariable Long spaceId) {

        // TODO 1. 유저가 이미 스페이스에 가입되어 있는 유저인지를 검증
        validateIsUserAlreadySpaceMember(userId, spaceId);

        // TODO 2. 초대할 스페이스의 정보를 return
        return new BaseResponse<>(spaceService.findSpaceJoin(spaceId));
    }

    private void validateIsUserAlreadySpaceMember(Long userId, Long spaceId) {
        // 유저가 이미 스페이스의 멤버일 경우 exception 발생
        // exception 을 발생시키는 것이 아니라 response에 유저가 스페이스에 가입된 유저인지에 대한 정보를 포함하는게 더 좋을까??
        userSpaceUtils.isUserAlreadySpaceMember(userId, spaceId);
    }

    /**
     * 스페이스 별 유저 프로필 정보 조회
     * 본인의 프로필 조회하는 경우 -> requestParam으로 userId 받지 X
     * 다른 멤버의 프로필 조회하는 경우 -> requestParam으로 userId 받음
     */
    @GetMapping("/{spaceId}/member-profile")
    public BaseResponse<GetUserProfileInSpaceDto.Response> getUserProfileInSpace(@JwtLoginAuth Long userId, @PathVariable Long spaceId, @RequestParam(name = "userId", required = false) Long targetUserId) {

        log.info("targetUserId={}", targetUserId);

        // TODO 1. 요청보내는 유저가 스페이스에 가입되어 있는지 검증
        validateIsUserInSpace(userId, spaceId);

        // TODO 2. 본인의 프로필을 조회하는지 다른 멤버의 프로필을 조회하는지 체크
        if (targetUserId == null) {
            targetUserId = userId;
        }

        log.info("targetUserId={}", targetUserId);

        // TODO 3. 유저 프로필 정보 return
        return new BaseResponse<>(spaceService.getUserProfileInSpace(targetUserId, spaceId));
    }

    private void validateIsUserInSpace(Long userId, Long spaceId) {
        userSpaceUtils.isUserInSpace(userId, spaceId);
    }
}
