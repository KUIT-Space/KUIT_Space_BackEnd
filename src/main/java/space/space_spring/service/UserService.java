package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import space.space_spring.dao.UserDao;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
}
