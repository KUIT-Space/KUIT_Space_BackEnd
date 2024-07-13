package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.dao.UserDao;
import space.space_spring.dto.PostUserRequest;
import space.space_spring.dto.PostUserResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    @Transactional
    public PostUserResponse signup(PostUserRequest postUserRequest) {
        // TODO 1. 이메일 중복 검사(아이디 중복 검사)

        // password 암호화도??

        // TODO 2. 회원정보 db insert
        Long savedUserId = userDao.saveUser(postUserRequest);

        // TODO 3. JWT 토큰 초기화 (회원가입시에는 토큰 발급 X)
        String jwt = null;

        return new PostUserResponse(savedUserId, jwt);
    }

}
