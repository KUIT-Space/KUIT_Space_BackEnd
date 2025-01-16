//package space.space_spring.repository;
//
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import space.space_spring.global.config.QueryDslConfig;
//import space.space_spring.dao.chat.ChatRoomDao;
//import space.space_spring.dao.chat.UserChatRoomDao;
//import space.space_spring.domain.chat.chatroom.model.ChatRoom;
//import space.space_spring.entity.Space;
//import space.space_spring.entity.User;
//import space.space_spring.domain.chat.chatting.model.UserChatRoom;
//import space.space_spring.entity.enumStatus.UserSignupType;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.*;
//
//@DataJpaTest
//@Import({QueryDslConfig.class, QueryDslConfig.class})
//@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
//public class UserChatRoomDaoTest {
//
//    @Autowired
//    private UserChatRoomDao userChatRoomDao;
//
//    @Autowired
//    private ChatRoomDao chatRoomDao;
//
//    private User testUser1;
//
//    private User testUser2;
//
//    private UserChatRoom testUserChatRoom1;
//
//    private UserChatRoom testUserChatRoom2;
//
//    private ChatRoom testChatRoom;
//
//    @BeforeEach
//    void 테스트_셋업() {
//        testUser1 = new User();
//        testUser2 = new User();
//
//        testUser1.saveUser("testUser1@test.com", "Asdf1234!", "testUser1", UserSignupType.LOCAL);
//        testUser2.saveUser("testUser2@test.com", "Asdf1234!", "testUser2", UserSignupType.LOCAL);
//
//        Space testSpace = new Space();
//
//        // testSpace에 속한 채팅방 생성 및 저장
//        testChatRoom = ChatRoom.of(testSpace, "testChatRoom", "");
//        chatRoomDao.save(testChatRoom);
//
//        // testChatRoom에 대한 testUserChatRoom 엔티티 생성
//        testUserChatRoom1 = UserChatRoom.of(testChatRoom, testUser1, LocalDateTime.now());
//        testUserChatRoom2 = UserChatRoom.of(testChatRoom, testUser2, LocalDateTime.now());
//    }
//
//    @Test
//    @DisplayName("유저채팅방_저장_테스트")
//    void 유저채팅방_저장_테스트() {
//        // given
//
//        // when
//        UserChatRoom savedTestUserChatRoom1 = userChatRoomDao.save(testUserChatRoom1);
//        UserChatRoom savedTestUserChatRoom2 = userChatRoomDao.save(testUserChatRoom2);
//
//        // then
//        assertThat(savedTestUserChatRoom1.getId()).isEqualTo(testUserChatRoom1.getId());
//        assertThat(savedTestUserChatRoom2.getId()).isEqualTo(testUserChatRoom2.getId());
//
//        assertThat(savedTestUserChatRoom1.getUser().getUserId()).isEqualTo(testUser1.getUserId());
//        assertThat(savedTestUserChatRoom2.getUser().getUserId()).isEqualTo(testUser2.getUserId());
//    }
//
//    @Test
//    @DisplayName("채팅방으로_유저채팅방_조회_테스트")
//    void 채팅방으로_유저채팅방_저장_테스트() {
//        // given
//        userChatRoomDao.save(testUserChatRoom1);
//        userChatRoomDao.save(testUserChatRoom2);
//
//        // when
//        List<UserChatRoom> userChatRoomList = userChatRoomDao.findByChatRoom(testChatRoom);
//
//        // then
//        assertThat(userChatRoomList.size()).isEqualTo(2);
//    }
//
//    @Test
//    @DisplayName("유저와_채팅방으로_유저채팅방_저장_테스트")
//    void 유저와_채팅방으로_유저채팅방_저장_테스트() {
//        // given
//        userChatRoomDao.save(testUserChatRoom1);
//        userChatRoomDao.save(testUserChatRoom2);
//
//        // when
//        UserChatRoom userChatRoom1 = userChatRoomDao.findByUserAndChatRoom(testUser1, testChatRoom);
//        UserChatRoom userChatRoom2 = userChatRoomDao.findByUserAndChatRoom(testUser2, testChatRoom);
//
//        // then
//        assertThat(userChatRoom1.getId()).isEqualTo(testUserChatRoom1.getId());
//        assertThat(userChatRoom2.getId()).isEqualTo(testUserChatRoom2.getId());
//    }
//}
