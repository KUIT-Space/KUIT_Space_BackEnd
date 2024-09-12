package space.space_spring.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dao.chat.ChatRoomDao;
import space.space_spring.dao.chat.ChattingDao;
import space.space_spring.dao.chat.UserChatRoomDao;
import space.space_spring.dto.chat.request.CreateChatRoomRequest;
import space.space_spring.dto.chat.request.JoinChatRoomRequest;
import space.space_spring.dto.chat.response.ChatSuccessResponse;
import space.space_spring.dto.chat.response.CreateChatRoomResponse;
import space.space_spring.dto.chat.response.ReadChatRoomMemberResponse;
import space.space_spring.dto.chat.response.ReadChatRoomResponse;
import space.space_spring.entity.*;
import space.space_spring.entity.enumStatus.UserSignupType;
import space.space_spring.entity.enumStatus.UserSpaceAuth;
import space.space_spring.util.space.SpaceUtils;
import space.space_spring.util.user.UserUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Mock
    private ChatRoomDao chatRoomDao;

    @Mock
    private UserUtils userUtils;

    @Mock
    private SpaceUtils spaceUtils;

    @Mock
    private UserSpaceDao userSpaceDao;

    @Mock
    private ChattingDao chattingDao;

    @Mock
    private UserChatRoomDao userChatRoomDao;

    private User user1;

    private User user2;

    private User user3;

    private Space testSpace;

    private UserSpace testUserSpace1;

    private UserSpace testUserSpace2;

    private UserSpace testUserSpace3;

    private ChatRoom chatRoom1;

    private ChatRoom chatRoom2;

    private UserChatRoom userChatRoom1;

    private UserChatRoom userChatRoom2;

    private UserChatRoom userChatRoom1ByUser2;

    private UserChatRoom userChatRoom1ByUser3;

    private CreateChatRoomRequest createChatRoomRequest1;

    private CreateChatRoomRequest createChatRoomRequest2;

    private JoinChatRoomRequest joinChatRoomRequest;

    private LocalDateTime mockTime;

    @BeforeEach
    void 채팅방_테스트_설정() {
        /**
         * <관리자인 user1은 chatRoom1, chatRoom2 생성>
         * <user1은 chatRoom1 생성 시 user2 포함, 추후에 user3을 초대>
         */
        user1 = createUser("user1@test.com", 0L);
        user2 = createUser("user2@test.com", 1L);
        user3 = createUser("user3@test.com", 2L);

        testSpace = new Space();
        testSpace.saveSpace("testSpace", "");
        ReflectionTestUtils.setField(testSpace, "spaceId", 0L);
        lenient().when(spaceUtils.findSpaceBySpaceId(0L)).thenReturn(testSpace);

        testUserSpace1 = new UserSpace();
        testUserSpace2 = new UserSpace();
        testUserSpace3 = new UserSpace();
        testUserSpace1.createUserSpace(this.user1, testSpace, UserSpaceAuth.MANAGER);
        testUserSpace2.createUserSpace(this.user2, testSpace, UserSpaceAuth.NORMAL);
        testUserSpace3.createUserSpace(user3, testSpace, UserSpaceAuth.NORMAL);

        mockTime = LocalDateTime.now();

        lenient().when(chatRoomDao.save(any(ChatRoom.class))).thenAnswer(invocationOnMock -> {
            ChatRoom savedChatRoom = invocationOnMock.getArgument(0);
            if ("chatRoom1".equals(savedChatRoom.getName())) {
                chatRoom1 = createChatRoom("chatRoom1", 0L);
                return chatRoom1;
            } else if ("chatRoom2".equals(savedChatRoom.getName())) {
                chatRoom2 = createChatRoom("chatRoom2", 1L);
                return chatRoom2;
            }
            return null;
        });

        // chatRoom1, chatRoom2 생성 시 사용할 request
        MockMultipartFile mockImgFile = new MockMultipartFile("mockImgFile", "test.png", "png", "test file".getBytes(StandardCharsets.UTF_8) );
        createChatRoomRequest1 = new CreateChatRoomRequest();
        createChatRoomRequest1.setName("chatRoom1");
        createChatRoomRequest1.setImg(mockImgFile);
        createChatRoomRequest1.setMemberList(List.of(1L));

        createChatRoomRequest2 = new CreateChatRoomRequest();
        createChatRoomRequest2.setName("chatRoom2");
        createChatRoomRequest2.setImg(mockImgFile);
        createChatRoomRequest2.setMemberList(List.of());

        // user3 초대 시 사용할 request
        joinChatRoomRequest = new JoinChatRoomRequest();
        ReflectionTestUtils.setField(joinChatRoomRequest, "memberList", List.of(2L));

        lenient().when(userChatRoomDao.save(any(UserChatRoom.class))).thenAnswer(invocationOnMock -> {
            UserChatRoom savedChatRoom = invocationOnMock.getArgument(0);
            if ("chatRoom1".equals(savedChatRoom.getChatRoom().getName())) {
                if ("user1".equals(savedChatRoom.getUser().getUserName())) {
                    userChatRoom1 = createUserChatRoom(0L, chatRoom1, user1);
                    return userChatRoom1;
                } else if ("user2".equals(savedChatRoom.getUser().getUserName())) {
                    userChatRoom1ByUser2 = createUserChatRoom(1L, chatRoom1, user2);
                    return userChatRoom1ByUser2;
                } else {
                    userChatRoom1ByUser3 = createUserChatRoom(2L, chatRoom1, user3);
                    return userChatRoom1ByUser3;
                }
            } else if ("chatRoom2".equals(savedChatRoom.getChatRoom().getName())) {
                userChatRoom2 = createUserChatRoom(3L, chatRoom2, user1);
                return userChatRoom2;
            }
            return null;
        });
    }

    @Test
    @DisplayName("특정_스페이스_내의_채팅방_생성_테스트")
    void 특정_스페이스_내의_채팅방_생성_테스트() {
        // given

        // when
        CreateChatRoomResponse createdChatRoom1 = chatRoomService.createChatRoom(user1.getUserId(), testSpace.getSpaceId(), createChatRoomRequest1, "");
        CreateChatRoomResponse createdChatRoom2 = chatRoomService.createChatRoom(user1.getUserId(), testSpace.getSpaceId(), createChatRoomRequest2, "");

        // then
        assertThat(createdChatRoom1.getChatRoomId()).isEqualTo(0L);
        assertThat(createdChatRoom2.getChatRoomId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("특정_스페이스_내의_전체_채팅방_조회_테스트")
    void 특정_스페이스_내의_전체_채팅방_조회_테스트() {
        // given
        chatRoomService.createChatRoom(user1.getUserId(), testSpace.getSpaceId(), createChatRoomRequest1, "");
        chatRoomService.createChatRoom(user1.getUserId(), testSpace.getSpaceId(), createChatRoomRequest2, "");

        when(chattingDao.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoom1.getId())).thenReturn(null); // 마지막 메시지가 없다고 가정
        when(chattingDao.countByChatRoomIdAndCreatedAtBetween(chatRoom1.getId(), convertToKoreaTime(mockTime), convertToKoreaTime(mockTime))).thenReturn(0);
        when(chatRoomDao.findByUserAndSpace(user1, testSpace)).thenReturn(List.of(chatRoom1, chatRoom2));
        when(userChatRoomDao.findByUserAndChatRoom(user1, chatRoom1)).thenReturn(userChatRoom1);

        when(chattingDao.findTopByChatRoomIdOrderByCreatedAtDesc(chatRoom2.getId())).thenReturn(null); // 마지막 메시지가 없다고 가정
        when(chattingDao.countByChatRoomIdAndCreatedAtBetween(chatRoom2.getId(), convertToKoreaTime(mockTime), convertToKoreaTime(mockTime))).thenReturn(0);
        when(userChatRoomDao.findByUserAndChatRoom(user1, chatRoom2)).thenReturn(userChatRoom2);

        // when
        ReadChatRoomResponse readChatRoomResponse = chatRoomService.readChatRooms(user1.getUserId(), testSpace.getSpaceId());

        // then
        assertThat(readChatRoomResponse.getChatRoomList().size()).isEqualTo(2);
        assertThat(readChatRoomResponse.getChatRoomList().get(0).getName()).isEqualTo("chatRoom1");
        assertThat(readChatRoomResponse.getChatRoomList().get(1).getName()).isEqualTo("chatRoom2");
    }

    @Test
    @DisplayName("특정_채팅방의_전체_멤버_조회_테스트")
    void 특정_채팅방의_전체_멤버_조회_테스트() {
        // given
        chatRoomService.createChatRoom(user1.getUserId(), testSpace.getSpaceId(), createChatRoomRequest1, "");
        chatRoomService.createChatRoom(user1.getUserId(), testSpace.getSpaceId(), createChatRoomRequest2, "");

        when(chatRoomDao.findById(chatRoom1.getId())).thenReturn(Optional.ofNullable(chatRoom1));
        when(userChatRoomDao.findByChatRoom(chatRoom1)).thenReturn(List.of(userChatRoom1, userChatRoom1ByUser2));
        when(userSpaceDao.findUserSpaceByUserAndSpace(user1, testSpace)).thenReturn(Optional.ofNullable(testUserSpace1));

        when(chatRoomDao.findById(chatRoom2.getId())).thenReturn(Optional.ofNullable(chatRoom2));
        when(userChatRoomDao.findByChatRoom(chatRoom2)).thenReturn(List.of(userChatRoom2));
        when(userSpaceDao.findUserSpaceByUserAndSpace(user2, testSpace)).thenReturn(Optional.ofNullable(testUserSpace2));

        // when
        ReadChatRoomMemberResponse readChatRoomMemberResponse1 = chatRoomService.readChatRoomMembers(testSpace.getSpaceId(), chatRoom1.getId());
        ReadChatRoomMemberResponse readChatRoomMemberResponse2 = chatRoomService.readChatRoomMembers(testSpace.getSpaceId(), chatRoom2.getId());

        // then
        assertThat(readChatRoomMemberResponse1.getUserList().size()).isEqualTo(2);
        assertThat(readChatRoomMemberResponse1.getUserList().get(0).getUserId()).isEqualTo(0L);
        assertThat(readChatRoomMemberResponse1.getUserList().get(1).getUserId()).isEqualTo(1L);
        assertThat(readChatRoomMemberResponse2.getUserList().size()).isEqualTo(1);
        assertThat(readChatRoomMemberResponse2.getUserList().get(0).getUserId()).isEqualTo(0L);
    }

    @Test
    @DisplayName("특정_채팅방으로의_멤버_초대_테스트")
    void 특정_채팅방으로의_멤버_초대_테스트() {
        // given
        chatRoomService.createChatRoom(user1.getUserId(), testSpace.getSpaceId(), createChatRoomRequest1, "");
        when(chatRoomDao.findById(chatRoom1.getId())).thenReturn(Optional.ofNullable(chatRoom1));
        when(userChatRoomDao.findByChatRoom(chatRoom1)).thenReturn(List.of(userChatRoom1, userChatRoom1ByUser2));

        // when
        ChatSuccessResponse chatSuccessResponse = chatRoomService.joinChatRoom(chatRoom1.getId(), joinChatRoomRequest);

        // then
        assertThat(chatSuccessResponse.isSuccess()).isEqualTo(true);
    }

    @Test
    @DisplayName("특정_채팅방에서_특정_유저가_마지막으로_읽은_시간_수정_테스트")
    void 특정_채팅방에서_특정_유저가_마지막으로_읽은_시간_수정_테스트() {
        // given
        chatRoomService.createChatRoom(user1.getUserId(), testSpace.getSpaceId(), createChatRoomRequest1, "");
        when(chatRoomDao.findById(chatRoom1.getId())).thenReturn(Optional.ofNullable(chatRoom1));
        when(userChatRoomDao.findByUserAndChatRoom(user1, chatRoom1)).thenReturn(userChatRoom1);

        // when
        ChatSuccessResponse chatSuccessResponse = chatRoomService.updateLastReadTime(user1.getUserId(), chatRoom1.getId());

        // then
        assertThat(chatSuccessResponse.isSuccess()).isEqualTo(true);
    }

    @Test
    @DisplayName("특정_채팅방_이름_수정_테스트")
    void 특정_채팅방_이름_수정_테스트() {
        // given
        chatRoomService.createChatRoom(user1.getUserId(), testSpace.getSpaceId(), createChatRoomRequest1, "");
        when(chatRoomDao.findById(chatRoom1.getId())).thenReturn(Optional.ofNullable(chatRoom1));

        // when
        ChatSuccessResponse chatSuccessResponse = chatRoomService.modifyChatRoomName(chatRoom1.getId(), "newChatRoom1");

        // then
        assertThat(chatSuccessResponse.isSuccess()).isEqualTo(true);
    }

    @Test
    @DisplayName("특정_채팅방_나가기_테스트")
    void 특정_채팅방_나가기_테스트() {
        // given
        chatRoomService.createChatRoom(user1.getUserId(), testSpace.getSpaceId(), createChatRoomRequest1, "");
        when(chatRoomDao.findById(chatRoom1.getId())).thenReturn(Optional.ofNullable(chatRoom1));
        when(userChatRoomDao.findByUserAndChatRoom(user2, chatRoom1)).thenReturn(userChatRoom1ByUser2);

        // when
        ChatSuccessResponse chatSuccessResponse = chatRoomService.exitChatRoom(user2.getUserId(), chatRoom1.getId());

        // then
        assertThat(chatSuccessResponse.isSuccess()).isEqualTo(true);
    }

    @Test
    @DisplayName("특정_채팅방_삭제_테스트")
    void 특정_채팅방_삭제_테스트() {
        // given
        chatRoomService.createChatRoom(user1.getUserId(), testSpace.getSpaceId(), createChatRoomRequest1, "");
        when(chatRoomDao.findById(chatRoom1.getId())).thenReturn(Optional.ofNullable(chatRoom1));

        // when
        ChatSuccessResponse chatSuccessResponse = chatRoomService.deleteChatRoom(chatRoom1.getId());

        // then
        assertThat(chatSuccessResponse.isSuccess()).isEqualTo(true);
    }

    private User createUser(String email, Long userId) {
        User user = new User();
        user.saveUser(email, "Asdf1234!", email.split("@")[0], UserSignupType.LOCAL);
        ReflectionTestUtils.setField(user, "userId", userId);
        lenient().when(userUtils.findUserByUserId(userId)).thenReturn(user);
        return user;
    }

    private ChatRoom createChatRoom(String name, Long id) {
        ChatRoom chatRoom = new ChatRoom();
        ReflectionTestUtils.setField(chatRoom, "id", id);
        ReflectionTestUtils.setField(chatRoom, "status", "ACTIVE");
        ReflectionTestUtils.setField(chatRoom, "createdAt", mockTime);
        ReflectionTestUtils.setField(chatRoom, "name", name);
        return chatRoom;
    }

    private UserChatRoom createUserChatRoom(Long id, ChatRoom chatRoom, User user) {
        UserChatRoom userChatRoom = new UserChatRoom();
        ReflectionTestUtils.setField(userChatRoom, "id", id);
        ReflectionTestUtils.setField(userChatRoom, "status", "ACTIVE");
        ReflectionTestUtils.setField(userChatRoom, "createdAt", mockTime);
        ReflectionTestUtils.setField(userChatRoom, "chatRoom", chatRoom);
        ReflectionTestUtils.setField(userChatRoom, "user", user);
        ReflectionTestUtils.setField(userChatRoom, "lastReadTime", mockTime);
        return userChatRoom;
    }

    private LocalDateTime convertToKoreaTime(LocalDateTime time) {
        return time.atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime();
    }
}
