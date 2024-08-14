package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dto.user.GetUserProfileListDto;
import space.space_spring.dto.user.dto.SpaceChoiceViewDto;
import space.space_spring.dto.user.request.PostUserLoginRequest;
import space.space_spring.dto.user.request.PostUserSignupRequest;
import space.space_spring.dto.user.response.GetSpaceInfoForUserResponse;
import space.space_spring.entity.UserSpace;
import space.space_spring.entity.enumStatus.UserSignupType;
import space.space_spring.exception.CustomException;
import space.space_spring.jwt.JwtLoginProvider;
import space.space_spring.dao.UserDao;
import space.space_spring.entity.User;
import space.space_spring.util.user.UserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static space.space_spring.entity.enumStatus.UserSignupType.LOCAL;
import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;
    private final JwtLoginProvider jwtLoginProvider;
    private final UserSpaceDao userSpaceDao;
    private final UserUtils userUtils;

    @Transactional
    public Long signup(PostUserSignupRequest postUserSignupRequest) {
        // TODO 1. 이메일 중복 검사(아이디 중복 검사)
        validateEmailForLocalSignup(postUserSignupRequest.getEmail());

        // password 암호화도??

        // TODO 2. 회원정보 db insert
        String email = postUserSignupRequest.getEmail();
        String password = postUserSignupRequest.getPassword();
        String userName = postUserSignupRequest.getUserName();

        User saveUser = userDao.saveUser(email, password, userName, LOCAL);

        return saveUser.getUserId();
    }

    private void validateEmailForLocalSignup(String email) {
        if (userDao.hasDuplicateEmail(email, UserSignupType.LOCAL)) {
            throw new CustomException(DUPLICATE_EMAIL);
        }
    }

    @Transactional
    public String login(PostUserLoginRequest postUserLoginRequest) {
        // TODO 1. 이메일 존재 여부 확인(아이디 존재 여부 확인)
        User userByEmail = userUtils.findUserByEmail(postUserLoginRequest.getEmail(), LOCAL);
        log.info("userByEmail.getUserId: {}", userByEmail.getUserId());

        // TODO 2. 비밀번호 일치 여부 확인
        validatePassword(userByEmail, postUserLoginRequest.getPassword());

        // TODO 3. JWT 발급
        String jwtLogin = jwtLoginProvider.generateToken(userByEmail);
        log.info("jwtLogin: {}", jwtLogin);

        return jwtLogin;
    }

    private void validatePassword(User userByEmail, String password) {
        if (!userByEmail.passwordMatch(password)) {
            throw new CustomException(PASSWORD_NO_MATCH);
        }
    }

    @Transactional
    public GetSpaceInfoForUserResponse getSpaceListForUser(Long userId, int size, Long lastUserSpaceId) {
        // TODO 1. userId로 User find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2. 특정 유저가 속해있는 스페이스 정보들을 get -> 무한 스크롤 구현
        SpaceChoiceViewDto spaceChoiceViewDto = userSpaceDao.getSpaceChoiceView(userByUserId, size, lastUserSpaceId);

        // TODO 3. find userName
        String userName = userByUserId.getUserName();

        // TODO 4. return
        return new GetSpaceInfoForUserResponse(userName, spaceChoiceViewDto.getLastUserSpaceId(), spaceChoiceViewDto.getSpaceChoiceInfoList());
    }

    @Transactional
    public GetUserProfileListDto.Response getUserProfileList(Long userId) {
        // TODO 1. userId로 User find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2. 유저가 속해있는 UserSpace list get
        List<UserSpace> userSpaceListByUser = userSpaceDao.findUserSpaceListByUser(userByUserId);

        // TODO 3. return
        List<GetUserProfileListDto.UserProfile> userProfileList = createUserProfileList(userSpaceListByUser);

        return new GetUserProfileListDto.Response(userProfileList);
    }

    private List<GetUserProfileListDto.UserProfile> createUserProfileList(List<UserSpace> userSpaceList) {
        List<GetUserProfileListDto.UserProfile> userProfileList = new ArrayList<>();

        for (UserSpace userSpace : userSpaceList) {
            GetUserProfileListDto.UserProfile userProfile = new GetUserProfileListDto.UserProfile(
                    userSpace.getSpace().getSpaceId(),
                    userSpace.getSpace().getSpaceName(),
                    userSpace.getUserName(),
                    userSpace.getUserProfileImg(),
                    userSpace.getUserSpaceAuth()
            );

            userProfileList.add(userProfile);
        }

        return userProfileList;
    }

}
