//package space.space_spring.repository;
//
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.context.annotation.Import;
//import space.space_spring.global.config.QueryDslConfig;
//import space.space_spring.domain.chat.chatroom.repository.ChatRoomRepository;
//import space.space_spring.domain.chat.chatroom.repository.UserChatRoomRepository;
//import space.space_spring.domain.chat.chatroom.model.ChatRoom;
//import space.space_spring.entity.Space;
//import space.space_spring.entity.User;
//import space.space_spring.domain.chat.chatroom.model.UserChatRoom;
//import space.space_spring.entity.enumStatus.BaseStatusType;
//import space.space_spring.entity.enumStatus.UserSignupType;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//
//@DataJpaTest
//@Import({QueryDslConfig.class, QueryDslConfig.class})
//public class ChatRoomRepositoryTest {
//
//    @Autowired
//    private ChatRoomRepository chatRoomRepository;
//
//    @Autowired
//    private UserChatRoomRepository userChatRoomRepository;
//
//    private Space testSpace;
//
//    private ChatRoom testChatRoom1;
//
//    private ChatRoom testChatRoom2;
//
//    private User testUser;
//
//    @BeforeEach
//    void 테스트_셋업() {
//        testUser = new User();
//
//        testUser.saveUser("testUser@test.com", "Asdf1234!", "testUser", UserSignupType.LOCAL);
//
//        testSpace = new Space();
//
//        // testSpace에 속한 chatRoom 엔티티 생성
//        testChatRoom1 = ChatRoom.of(testSpace, "testChatRoom1", "");
//        testChatRoom2 = ChatRoom.of(testSpace, "testChatRoom2", "");
//    }
//
//    @Test
//    @DisplayName("채팅방_저장_테스트")
//    void 채팅방_저장_테스트() {
//        // given
//
//        // when
//        ChatRoom savedTestChatRoom1 = chatRoomRepository.save(testChatRoom1);
//        ChatRoom savedTestChatRoom2 = chatRoomRepository.save(testChatRoom2);
//
//        // then
//        assertThat(savedTestChatRoom1.getId()).isEqualTo(testChatRoom1.getId());
//        assertThat(savedTestChatRoom2.getId()).isEqualTo(testChatRoom2.getId());
//    }
//
//    @Test
//    @DisplayName("id로_채팅방_조회_테스트")
//    void id로_채팅방_조회_테스트() {
//        // given
//        ChatRoom savedTestChatRoom1 = chatRoomRepository.save(testChatRoom1);
//        ChatRoom savedTestChatRoom2 = chatRoomRepository.save(testChatRoom2);
//
//        // when
//        Optional<ChatRoom> chatRoom1byId = Optional.ofNullable(
//                chatRoomRepository.findByIdAndStatus(savedTestChatRoom1.getId(), BaseStatusType.ACTIVE));
//        Optional<ChatRoom> chatRoom2byId = Optional.ofNullable(
//                chatRoomRepository.findByIdAndStatus(savedTestChatRoom2.getId(), BaseStatusType.ACTIVE));
//
//        // then
//        chatRoom1byId.ifPresent(chatRoom -> assertThat(chatRoom.getId()).isEqualTo(savedTestChatRoom1.getId()));
//        chatRoom2byId.ifPresent(chatRoom -> assertThat(chatRoom.getId()).isEqualTo(savedTestChatRoom2.getId()));
//    }
//
//    @Test
//    @DisplayName("유저와_스페이스로_채팅방_조회_테스트")
//    void 유저와_스페이스로_채팅방_조회_테스트() {
//        // given
//        ChatRoom savedTestChatRoom1 = chatRoomRepository.save(testChatRoom1);
//        ChatRoom savedTestChatRoom2 = chatRoomRepository.save(testChatRoom2);
//        UserChatRoom userChatRoom1 = UserChatRoom.of(savedTestChatRoom1, testUser, LocalDateTime.now());
//        UserChatRoom userChatRoom2 = UserChatRoom.of(savedTestChatRoom2, testUser, LocalDateTime.now());
//        userChatRoomRepository.save(userChatRoom1);
//        userChatRoomRepository.save(userChatRoom2);
//
//        // when
//        List<ChatRoom> chatRoomListByUser = chatRoomRepository.findByUserAndSpace(testUser, testSpace);
//
//        // then
//        assertThat(chatRoomListByUser.size()).isEqualTo(2);
//    }
//}
