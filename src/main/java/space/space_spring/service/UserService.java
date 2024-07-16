package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dto.jwt.JwtPayloadDto;
import space.space_spring.jwt.JwtLoginProvider;
import space.space_spring.jwt.JwtUserSpaceProvider;
import space.space_spring.dao.UserDao;
import space.space_spring.entity.User;
import space.space_spring.dto.user.PostUserLoginRequest;
import space.space_spring.dto.user.PostUserSignupRequest;
import space.space_spring.dto.user.PostUserSignupResponse;
import space.space_spring.exception.UserException;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final JwtLoginProvider jwtLoginProvider;

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
        if (!userByEmail.getPassword().equals(password)) {
            throw new UserException(PASSWORD_NO_MATCH);
        }
    }
}
