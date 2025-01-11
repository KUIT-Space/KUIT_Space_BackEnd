package space.space_spring.domain.space.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.argumentResolver.userSpace.CheckUserSpace;
import space.space_spring.domain.space.model.dto.GetSpaceJoinDto;
import space.space_spring.domain.space.model.dto.PostSpaceJoinDto;
import space.space_spring.domain.space.model.request.PostSpaceCreateDto;
import space.space_spring.domain.space.model.response.GetUserInfoBySpaceResponse;

import space.space_spring.domain.userSpace.model.GetUserProfileInSpaceDto;
import space.space_spring.domain.userSpace.model.PutUserProfileInSpaceDto;
import space.space_spring.domain.space.model.entity.Space;
import space.space_spring.global.exception.CustomException;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.domain.pay.service.PayService;
import space.space_spring.domain.board.service.PostService;
import space.space_spring.global.util.S3Uploader;
import space.space_spring.domain.space.service.SpaceService;
import space.space_spring.global.util.userSpace.UserSpaceUtils;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import java.io.IOException;

import static space.space_spring.global.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space")
@Slf4j
public class SpaceController {

    private final SpaceService spaceService;
    private final S3Uploader s3Uploader;
    private final String spaceImgDirName = "spaceImg";
    private final String userProfileImgDirName = "userProfileImg";
    private final UserSpaceUtils userSpaceUtils;
    private final PayService payService;
    private final PostService postService;

    @PostMapping("")
    public BaseResponse<PostSpaceCreateDto.Response> createSpace(@JwtLoginAuth Long userId, @Validated @ModelAttribute PostSpaceCreateDto.Request postSpaceCreateRequest, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_SPACE_CREATE, getErrorMessage(bindingResult));
        }

        // TODO 1. 스페이스 썸네일을 s3에 upload
        String spaceImgUrl = processSpaceImage(postSpaceCreateRequest.getSpaceProfileImg());

        // TODO 2. s3에 저장하고 받은 이미지 url 정보와 spaceName 정보로 space create 작업 수행
        Space createSpace = spaceService.createSpace(userId, postSpaceCreateRequest.getSpaceName(), spaceImgUrl);

        return new BaseResponse<>(new PostSpaceCreateDto.Response(createSpace.getSpaceId()));
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
            throw new CustomException(IS_NOT_IMAGE_FILE);
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
    @CheckUserSpace(required = false)
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

    /**
     * 스페이스 별 유저 프로필 정보 수정
     */
    @PutMapping("/{spaceId}/member-profile")
    public BaseResponse<PutUserProfileInSpaceDto.Response> updateUserProfileInSpace(@JwtLoginAuth Long userId, @PathVariable Long spaceId, @Validated @ModelAttribute PutUserProfileInSpaceDto.Request request, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_USER_SPACE_PROFILE, getErrorMessage(bindingResult));
        }

        // TODO 1. 유저가 스페이스에 가입되어 있는지 검증
        validateIsUserInSpace(userId, spaceId);

        // TODO 2. 유저 프로필 썸네일을 s3에 upload
        String userProfileImgUrl = processUserProfileImage(request.getUserProfileImg());

        // TODO 3. 유저 프로필 정보 update
        PutUserProfileInSpaceDto putUserProfileInSpaceDto = new PutUserProfileInSpaceDto(
                request.getUserName(),
                userProfileImgUrl,
                request.getUserProfileMsg()
        );

        return new BaseResponse<>(spaceService.changeUserProfileInSpace(userId, spaceId, putUserProfileInSpaceDto));
    }

    private String processUserProfileImage(MultipartFile userProfileImg) throws IOException {
        if (userProfileImg == null) {
            return null;
        }
        validateImageFile(userProfileImg);
        return s3Uploader.upload(userProfileImg, userProfileImgDirName);
    }

    /**
     * 유저의 스페이스 가입 처리
     */
    @CheckUserSpace(required = false)
    @PostMapping("/{spaceId}/join")
    public BaseResponse<String> joinUserToSpace(@JwtLoginAuth Long userId, @PathVariable Long spaceId, @Validated @ModelAttribute PostSpaceJoinDto.Request request, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new CustomException(INVALID_SPACE_JOIN_REQUEST, getErrorMessage(bindingResult));
        }

        // TODO 1. 유저가 스페이스에 가입되어 있는지 검증
        validateIsUserAlreadySpaceMember(userId, spaceId);

        // TODO 2. 유저 프로필 썸네일을 s3에 upload
        String userProfileImgUrl = processUserProfileImage(request.getUserProfileImg());

        // TODO 3. 유저의 스페이스 가입 처리
        PostSpaceJoinDto postSpaceJoinDto = new PostSpaceJoinDto(
                userProfileImgUrl,
                request.getUserName(),
                request.getUserProfileMsg()
        );

        spaceService.createUserSpace(userId, spaceId, postSpaceJoinDto);

        return new BaseResponse<>("유저의 스페이스 가입 처리 성공");
    }

//    /**
//     * 스페이스 홈 화면
//     */
//    @GetMapping("/{spaceId}")
//    public BaseResponse<GetSpaceHomeDto.Response> showSpaceHome(@JwtLoginAuth Long userId, @PathVariable Long spaceId, @UserSpaceId Long userSpaceId, @UserSpaceAuth String userAuth) {
//        log.info("userId = {}, spaceId = {}, userSpaceID = {}, userAuth = {}", userId, spaceId, userSpaceId, userAuth);
//
//        // TODO 1. 스페이스 정보 get
//        GetSpaceHomeDto.SpaceInfoForHome spaceInfoForHome = spaceService.getSpaceInfoForHome(spaceId);
//
//        // TODO 2. 해당 스페이스에서의 유저 정산 정보 get
//        // 유저가 요청한 정산 중 현재 진행중인 정산 리스트
//        // AND
//        // 유저가 요청받은 정산 중 현재 진행중인 정산 리스트
//        List<PayRequestInfoDto> payRequestInfoForUser = payService.getPayRequestInfoForUser(userId, spaceId, false);
//        List<PayTargetInfoDto> payReceiveInfoForUser = payService.getPayReceiveInfoForUser(userId, spaceId, false);
//
//        // TODO 3. 해당 스페이스의 공지사항 get
//        List<GetSpaceHomeDto.SpaceHomeNotice> noticeInfoForHome = postService.getNoticeInfoForHome(spaceId);
//
//        // TODO 4. return
//        return new BaseResponse<>(new GetSpaceHomeDto.Response(
//                spaceInfoForHome.getSpaceName(),
//                spaceInfoForHome.getSpaceProfileImg(),
//                payRequestInfoForUser,
//                payReceiveInfoForUser,
//                noticeInfoForHome,
//                spaceInfoForHome.getMemberNum(),
//                userAuth
//        ));
//    }
}
