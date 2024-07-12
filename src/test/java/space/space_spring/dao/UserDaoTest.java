package space.space_spring.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import space.space_spring.domain.User;
import space.space_spring.dto.PostUserRequest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoTest {

    @Autowired UserDao userDao;

    @Transactional
    @Rollback(value = false)
    @Test
    @DisplayName("유저_회원가입_테스트")
    void 유저_회원가입_테스트() throws Exception {
        //given
        String email = "test@test.com";
        String password = "123456abC!";
        String name = "노성준";
        PostUserRequest postUserRequest = new PostUserRequest(email, password, name);

        //when
        Long savedUserId = userDao.saveUser(postUserRequest);
        User findUser = userDao.findUserByEmail(email);

        //then
        Assertions.assertThat(savedUserId).isEqualTo(findUser.getUserId());
    }

    @Transactional
    @Test
    @DisplayName("유저_회원가입_실패_테스트")
    void 유저_회원가입_실패_테스트 () throws Exception {
        //given
        String email = "test@test.com";
        String password = "123456";         // 비밀번호 validation 벗어난 경우
        String name = "노성준";
        PostUserRequest postUserRequest = new PostUserRequest(email, password, name);

        // 테스트 케이스를 작성하기 위해서는 exception을 정의해야함
//        //when
//        Long savedUserId = userDao.saveUser(postUserRequest);
//        User findUser = userDao.findUserByEmail(email);
//
//        //then
    }




}