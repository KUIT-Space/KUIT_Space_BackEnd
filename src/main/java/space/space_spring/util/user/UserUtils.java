package space.space_spring.util.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.UserDao;
import space.space_spring.entity.User;
import space.space_spring.exception.UserException;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;

@Component
@RequiredArgsConstructor
public class UserUtils {

    private final UserDao userDao;

    @Transactional
    public User findUserByEmail(String email) {
        User findUser = userDao.getUserByEmail(email);
        if (findUser == null) {
            throw new UserException(EMAIL_NOT_FOUND);
        }
        return findUser;
    }

    @Transactional
    public User findUserByUserId(Long userId) {
        User userByUserId = userDao.findUserByUserId(userId);
        if (userByUserId == null) {
            throw new UserException(USER_NOT_FOUND);
        }
        return userByUserId;
    }

    @Transactional
    public void validateEmail(String email) {
        if (userDao.hasDuplicateEmail(email)) {
            throw new UserException(DUPLICATE_EMAIL);
        }
    }
}
