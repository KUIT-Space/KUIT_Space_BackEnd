package space.space_spring.service;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {

//    @Autowired
//    UserService userService;
//
//    @Autowired
//    UserDao userDao;
//
//    @Transactional
//    @Rollback(value = false)
//    @Test
//    @DisplayName("유저_회원가입_테스트")
//    void 유저_회원가입_테스트() throws Exception {
//        //given
//        String email = "test@test.com";
//        String password = "123456abC!";
//        String name = "노성준";
//        PostUserSignupRequest postUserRequest = new PostUserSignupRequest(email, password, name);
//
//        //when
//        PostUserSignupResponse postUserResponse = userService.signup(postUserRequest);
//        User findUser = userDao.findUserByEmail(email);
//
//        //then
//        Assertions.assertThat(postUserResponse.getUserId()).isEqualTo(findUser.getUserId());
//    }

//    @Transactional
//    @Test
//    @DisplayName("유저_회원가입_실패_테스트")
//    void 유저_회원가입_실패_테스트 () throws Exception {
//        //given
//        String email = "test@test.com";
//        String password = "123456";         // 비밀번호 validation 벗어난 경우
//        String name = "노성준";
//        PostUserRequest postUserRequest = new PostUserRequest(email, password, name);
//
//        //when
//        // 비밀번호 유효성 검사에 실패하여 예외를 발생할 것임
//        Assertions.assertThatThrownBy(() -> {
//            userDao.saveUser(postUserRequest);
//        }).isInstanceOf(UserException.class);
//
//        //then
//        // findUser는 null 일 것임
//        User findUser = userDao.findUserByEmail(email);
//        Assertions.assertThat(findUser).isNull();
//
//    }

}