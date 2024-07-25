package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dto.user.*;
import space.space_spring.entity.enumStatus.UserSignupType;
import space.space_spring.jwt.JwtLoginProvider;
import space.space_spring.dao.UserDao;
import space.space_spring.entity.User;
import space.space_spring.exception.UserException;
import space.space_spring.response.BaseResponse;
import space.space_spring.util.user.UserUtils;

import java.util.List;
import java.util.Map;

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
    public PostUserSignupResponse signup(PostUserSignupRequest postUserSignupRequest) {
        // TODO 1. 이메일 중복 검사(아이디 중복 검사)
        userUtils.validateEmail(postUserSignupRequest.getEmail());

        // password 암호화도??

        // TODO 2. 회원정보 db insert
        String email = postUserSignupRequest.getEmail();
        String password = postUserSignupRequest.getPassword();
        String userName = postUserSignupRequest.getUserName();

        User saveUser = userDao.saveUser(email, password, userName, LOCAL);

        return new PostUserSignupResponse(saveUser.getUserId());
    }

    @Transactional
    public String login(PostUserLoginRequest postUserLoginRequest) {
        // TODO 1. 이메일 존재 여부 확인(아이디 존재 여부 확인)
        User userByEmail = userUtils.findUserByEmail(postUserLoginRequest.getEmail());
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
            throw new UserException(PASSWORD_NO_MATCH);
        }
    }

    @Transactional
    public GetSpaceInfoForUserResponse getSpaceListForUser(Long userId, int size, Long lastUserSpaceId) {
        // TODO 1. userId로 User find
        User userByUserId = userUtils.findUserByUserId(userId);

        // TODO 2. user가 속한 스페이스가 없는 경우 -> 예외처리 ??
        // (현재 lastUserSpaceId가 -1 & 스페이스 info list는 빈 껍데기로 response가 전달됨)
        validateSpaceListForUser(userByUserId);

        // TODO 3. 특정 유저가 속해있는 스페이스 정보들을 get -> 무한 스크롤 구현
        SpaceChoiceViewDto spaceChoiceViewDto = userSpaceDao.getSpaceChoiceView(userByUserId, size, lastUserSpaceId);

        // TODO 4. find userName
        String userName = userByUserId.getUserName();

        // TODO 5. return
        return new GetSpaceInfoForUserResponse(userName, spaceChoiceViewDto.getLastUserSpaceId(), spaceChoiceViewDto.getSpaceNameAndProfileImgList());
    }

    private void validateSpaceListForUser(User userByUserId) {
        // 프론트 개발자 분들과 상의해서 결정해야할 거 같음

    }

}
