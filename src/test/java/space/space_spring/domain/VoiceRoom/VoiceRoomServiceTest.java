package space.space_spring.domain.VoiceRoom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dao.VoiceRoomDao;
import space.space_spring.dao.VoiceRoomRepository;
import space.space_spring.dto.VoiceRoom.PostVoiceRoomDto;
import space.space_spring.entity.*;
import space.space_spring.entity.enumStatus.UserSignupType;
import space.space_spring.service.VoiceRoomService;
import space.space_spring.util.LiveKitUtils;
import space.space_spring.util.space.SpaceUtils;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoiceRoomServiceTest {
    @InjectMocks
    private VoiceRoomService voiceRoomService;

    @Mock
    private VoiceRoomRepository voiceRoomRepository;

    @Mock
    private VoiceRoomDao voiceRoomDao;

    @Mock
    // 삭제 예정
    private SpaceUtils spaceUtils;

    @Mock
    private LiveKitUtils liveKitUtils;



    @Mock
    private UserSpaceDao userSpaceDao;


    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;
    private User user6;
    private User user7;
    private Space testSpace;
    private PayRequest testPayRequest_user1;
    private PayRequest testPayRequest_user2;
    private PayRequestTarget testPayRequestTarget_user1;
    private PayRequestTarget testPayRequestTarget_user2;
    private PayRequestTarget testPayRequestTarget_user3;
    private PayRequestTarget testPayRequestTarget_user4;

    @BeforeEach
    public void 테스트_셋업() {
        /**
         * <user1이 같은 스페이스에 속한 user2, 3, 4 에게 정산을 요청> : user2, 3은 정산 완료 X & user4는 정산 완료
         * <user2는 user1에게 정산을 요청> : user1은 정산 완료
         */
        user1 = new User();
        user1.saveUser("test1@test.com", "abcDEF123!@", "user1", UserSignupType.LOCAL);

        user2 = new User();
        user2.saveUser("test2@test.com", "abcDEF123!@", "user2", UserSignupType.LOCAL);

        user3 = new User();
        user3.saveUser("test3@test.com", "abcDEF123!@", "user3", UserSignupType.LOCAL);

        user4 = new User();
        user4.saveUser("test4@test.com", "abcDEF123!@", "user4", UserSignupType.LOCAL);

        testSpace = new Space();
        testSpace.saveSpace("testSpace", "test_profile_img_url");

        testPayRequest_user1 = new PayRequest();
        testPayRequest_user1.savePayRequest(user1, testSpace, 30000, "우리은행", "111-111-111", 0, false);

        testPayRequestTarget_user2 = new PayRequestTarget();
        testPayRequestTarget_user2.savePayRequestTarget(testPayRequest_user1, user2.getUserId(), 10000, false);

        testPayRequestTarget_user3 = new PayRequestTarget();
        testPayRequestTarget_user3.savePayRequestTarget(testPayRequest_user1, user3.getUserId(), 10000, false);

        testPayRequestTarget_user4 = new PayRequestTarget();
        testPayRequestTarget_user4.savePayRequestTarget(testPayRequest_user1, user4.getUserId(), 10000, true);

        testPayRequest_user2 = new PayRequest();
        testPayRequest_user2.savePayRequest(user2, testSpace, 10000, "국민은행", "111-111-111", 0, true);

        testPayRequestTarget_user1 = new PayRequestTarget();
        testPayRequestTarget_user1.savePayRequestTarget(testPayRequest_user2, user1.getUserId(), 10000, true);

        /**
         * 추가로 user5,6,7 생성
         */
        user5 = new User();
        user6 = new User();
        user7 = new User();
        user5.saveUser("test5@test.com", "abcDEF123!@", "user5", UserSignupType.LOCAL);
        user6.saveUser("test6@test.com", "abcDEF123!@", "user6", UserSignupType.LOCAL);
        user7.saveUser("test7@test.com", "abcDEF123!@", "user7", UserSignupType.LOCAL);

    }

    @Test
    @DisplayName("create VoiceRoom test")
    void voiceRoom_create_test(){
        //given
        int spaceOrder =1;
        String voiceRoomName = "voiceRoom";
        Long spaceId = 2L;
        //when
        //when(testSpace.getSpaceId()).thenReturn(1L);
        //testSpace.
        ReflectionTestUtils.setField(testSpace,"spaceId",spaceId);
        when(spaceUtils.findSpaceBySpaceId(testSpace.getSpaceId())).thenReturn(testSpace);
        when(voiceRoomRepository.findMaxOrderBySpace(testSpace)).thenReturn(spaceOrder);
        when(voiceRoomDao.createVoiceRoom(voiceRoomName,spaceOrder+1,testSpace)).thenReturn(1L);


        PostVoiceRoomDto.Request request = new PostVoiceRoomDto.Request(voiceRoomName);
        Long voiceRoomId = voiceRoomService.createVoiceRoom(testSpace.getSpaceId(),request);

        //then
        assertThat(voiceRoomId).isEqualTo(1L);

    }
}
