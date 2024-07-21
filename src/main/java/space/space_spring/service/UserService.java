package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dto.user.*;
import space.space_spring.jwt.JwtLoginProvider;
import space.space_spring.dao.UserDao;
import space.space_spring.entity.User;
import space.space_spring.exception.UserException;
import space.space_spring.response.BaseResponse;

import java.util.List;
import java.util.Map;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserDao userDao;
    private final JwtLoginProvider jwtLoginProvider;
    private final UserSpaceDao userSpaceDao;

    @Transactional
    public PostUserSignupResponse signup(PostUserSignupRequest postUserSignupRequest) {
        // TODO 1. 이메일 중복 검사(아이디 중복 검사)
        validateEmail(postUserSignupRequest.getEmail());

        // password 암호화도??


        // TODO 2. 회원정보 db insert
        User saveUser = userDao.saveUser(postUserSignupRequest);

        // TODO 3. JWT 토큰 초기화 (회원가입시에는 토큰 발급 X)
        String jwt = null;

        return new PostUserSignupResponse(saveUser.getUserId(), jwt);
    }

    private void validateEmail(String email) {
        if (userDao.hasDuplicateEmail(email)) {
            throw new UserException(DUPLICATE_EMAIL);
        }
    }

    @Transactional
    public String login(PostUserLoginRequest postUserLoginRequest) {
        // TODO 1. 이메일 존재 여부 확인(아이디 존재 여부 확인)
        User userByEmail = findUserByEmail(postUserLoginRequest.getEmail());
        log.info("userByEmail.getUserId: {}", userByEmail.getUserId());

        // TODO 2. 비밀번호 일치 여부 확인
        validatePassword(userByEmail, postUserLoginRequest.getPassword());

        // TODO 3. JWT 발급
        String jwtLogin = jwtLoginProvider.generateToken(userByEmail);

        // TODO 4. JWT db에 insert -> db에 저장해야할까??
        userByEmail.saveJWTtoLoginUser(jwtLogin);

        return jwtLogin;
    }

    private User findUserByEmail(String email) {
        User findUser = userDao.getUserByEmail(email);
        if (findUser == null) {
            throw new UserException(EMAIL_NOT_FOUND);
        }
        return findUser;
    }

    private void validatePassword(User userByEmail, String password) {
        if (!userByEmail.passwordMatch(password)) {
            throw new UserException(PASSWORD_NO_MATCH);
        }
    }

    @Transactional
    public GetSpaceInfoForUserResponse getSpaceListForUser(Long userId, int size, Long lastUserSpaceId) {
        // TODO 1. userId로 User find
        User userByUserId = findUserByUserId(userId);

        // TODO 2. 특정 유저가 속해있는 스페이스 정보들을 get -> 무한 스크롤 구현
        SpaceChoiceViewDto spaceChoiceViewDto = userSpaceDao.getSpaceChoiceView(userByUserId, size, lastUserSpaceId);

        // TODO 3. find userName
        String userName = userByUserId.getUserName();

        // TODO 4. return
        return new GetSpaceInfoForUserResponse(userName, spaceChoiceViewDto.getLastUserSpaceId(), spaceChoiceViewDto.getSpaceNameAndProfileImgList());
    }

    private User findUserByUserId(Long userId) {
        User userByUserId = userDao.findUserByUserId(userId);
        if (userByUserId == null) {
            throw new UserException(USER_NOT_FOUND);
        }
        return userByUserId;
    }

}
