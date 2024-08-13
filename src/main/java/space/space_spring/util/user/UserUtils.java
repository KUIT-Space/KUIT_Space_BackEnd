package space.space_spring.util.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.UserDao;
import space.space_spring.entity.User;
import space.space_spring.entity.enumStatus.UserSignupType;
import space.space_spring.exception.CustomException;

import java.util.UUID;

import static space.space_spring.entity.enumStatus.UserSignupType.LOCAL;
import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final UserDao userDao;

    @Transactional
    public User findUserByEmail(String email) {
        User findUser = userDao.getUserByEmail(email);
        if (findUser == null) {
            throw new CustomException(EMAIL_NOT_FOUND);
        }
        return findUser;
    }

    @Transactional
    public User findUserByUserId(Long userId) {
        User userByUserId = userDao.findUserByUserId(userId);
        if (userByUserId == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
        return userByUserId;
    }

    @Transactional
    public User findOrCreateUserForOAuthInfo(String email, String nickname, UserSignupType signupType) {
        // email 이 존재 -> 이미 소셜로그인으로 회원가입한 유저
        // email 이 존재하지 X -> 소셜로그인 처음 하는 유저 -> db에 유저정보 저장

        if (userDao.hasDuplicateEmail(email, signupType)) {
            return userDao.findUserByEmailAndSignupType(email, signupType);
        }

        String password = UUID.randomUUID().toString();
        return userDao.saveUser(email, password, nickname, signupType);
    }
}
