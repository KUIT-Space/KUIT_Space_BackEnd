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
        Long savedUserId = userDao.saveUser(postUserRequest);
        String jwt = null;
        return new PostUserResponse(savedUserId, jwt);
    }

}
