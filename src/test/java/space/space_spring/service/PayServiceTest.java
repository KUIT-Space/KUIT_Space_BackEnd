package space.space_spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.space_spring.dao.PayDao;
import space.space_spring.dto.pay.PayReceiveInfoDto;
import space.space_spring.dto.pay.PayRequestInfoDto;
import space.space_spring.dto.pay.PostPayCreateRequest;
import space.space_spring.entity.PayRequest;
import space.space_spring.entity.PayRequestTarget;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.enumStatus.UserSignupType;
import space.space_spring.util.space.SpaceUtils;
import space.space_spring.util.user.UserUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PayServiceTest {

    @InjectMocks
    private PayService payService;

    @Mock
    private PayDao payDao;

    @Mock
    private UserUtils userUtils;

    @Mock
    private SpaceUtils spaceUtils;

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
        testPayRequest_user1.savePayRequest(user1, testSpace, 30000, "우리은행", "111-111-111", false);

        testPayRequestTarget_user2 = new PayRequestTarget();
        testPayRequestTarget_user2.savePayRequestTarget(testPayRequest_user1, user2.getUserId(), 10000, false);

        testPayRequestTarget_user3 = new PayRequestTarget();
        testPayRequestTarget_user3.savePayRequestTarget(testPayRequest_user1, user3.getUserId(), 10000, false);

        testPayRequestTarget_user4 = new PayRequestTarget();
        testPayRequestTarget_user4.savePayRequestTarget(testPayRequest_user1, user4.getUserId(), 10000, true);

        testPayRequest_user2 = new PayRequest();
        testPayRequest_user2.savePayRequest(user2, testSpace, 10000, "국민은행", "111-111-111", true);

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
    @DisplayName("유저가_요청한_정산중_현재진행중인_정산리스트_찾기")
    void 유저가_요청한_정산중_현재진행중인_정산리스트_찾기() throws Exception {
        //given
        // user1이 user2, 3, 4 에게 요청한 정산
        when(userUtils.findUserByUserId(user1.getUserId())).thenReturn(user1);
        when(spaceUtils.findSpaceBySpaceId(testSpace.getSpaceId())).thenReturn(testSpace);
        when(payDao.findPayRequestListByUser(user1, testSpace, false)).thenReturn(List.of(testPayRequest_user1));
        when(payDao.findPayRequestTargetListByPayRequest(testPayRequest_user1)).thenReturn(List.of(testPayRequestTarget_user2, testPayRequestTarget_user3, testPayRequestTarget_user4));

        //when
        List<PayRequestInfoDto> payRequestInfoForUser = payService.getPayRequestInfoForUser(user1.getUserId(), testSpace.getSpaceId(), false);

        //then
        assertThat(payRequestInfoForUser.size()).isEqualTo(1);

        for (PayRequestInfoDto payRequestInfoDto : payRequestInfoForUser) {
            assertThat(payRequestInfoDto.getTotalAmount()).isEqualTo(30000);
            assertThat(payRequestInfoDto.getReceiveAmount()).isEqualTo(10000);
            assertThat(payRequestInfoDto.getTotalTargetNum()).isEqualTo(3);
            assertThat(payRequestInfoDto.getReceiveTargetNum()).isEqualTo(1);
        }
    }

    @Test
    @DisplayName("유저가_요청받은_정산중_현재진행중인_정산리스트_찾기")
    void 유저가_요청받은_정산중_현재진행중인_정산리스트_찾기() throws Exception {
        //given
        // user2가 user1에게 요청받은 정산
        when(userUtils.findUserByUserId(user2.getUserId())).thenReturn(user2);
        when(spaceUtils.findSpaceBySpaceId(testSpace.getSpaceId())).thenReturn(testSpace);
        when(payDao.findPayRequestTargetListByUser(user2, testSpace, false)).thenReturn(List.of(testPayRequestTarget_user2));

        //when
        List<PayReceiveInfoDto> payReceiveInfoForUser = payService.getPayReceiveInfoForUser(user2.getUserId(), testSpace.getSpaceId(), false);

        //then
        assertThat(payReceiveInfoForUser.size()).isEqualTo(1);
        for (PayReceiveInfoDto payReceiveInfoDto : payReceiveInfoForUser) {
            assertThat(payReceiveInfoDto.getPayCreatorName()).isEqualTo(user1.getUserName());
            assertThat(payReceiveInfoDto.getRequestAmount()).isEqualTo(10000);
        }
    }

    @Test
    @DisplayName("유저가_요청한_정산중_완료된_정산리스트_찾기")
    void 유저가_요청한_정산중_완료된_정산리스트_찾기() throws Exception {
        //given
        // user2가 user1에게 요청한 정산
        when(userUtils.findUserByUserId(user2.getUserId())).thenReturn(user2);
        when(spaceUtils.findSpaceBySpaceId(testSpace.getSpaceId())).thenReturn(testSpace);
        when(payDao.findPayRequestListByUser(user2, testSpace, true)).thenReturn(List.of(testPayRequest_user2));
        when(payDao.findPayRequestTargetListByPayRequest(testPayRequest_user2)).thenReturn(List.of(testPayRequestTarget_user1));

        //when
        List<PayRequestInfoDto> payRequestInfoForUser = payService.getPayRequestInfoForUser(user2.getUserId(), testSpace.getSpaceId(), true);

        //then
        assertThat(payRequestInfoForUser.size()).isEqualTo(1);

        for (PayRequestInfoDto payRequestInfoDto : payRequestInfoForUser) {
            assertThat(payRequestInfoDto.getTotalAmount()).isEqualTo(10000);
            assertThat(payRequestInfoDto.getReceiveAmount()).isEqualTo(10000);
            assertThat(payRequestInfoDto.getTotalTargetNum()).isEqualTo(1);
            assertThat(payRequestInfoDto.getReceiveTargetNum()).isEqualTo(1);
        }
    }

    @Test
    @DisplayName("정산_생성_테스트")
    void 정산_생성_테스트() throws Exception {
        //given
        // user5가 user6, 7 에게 정산 생성
        PostPayCreateRequest.TargetInfo testTargetInfo1 = new PostPayCreateRequest.TargetInfo(user6.getUserId(), 10000);
        PostPayCreateRequest.TargetInfo testTargetInfo2 = new PostPayCreateRequest.TargetInfo(user7.getUserId(), 10000);

        List<PostPayCreateRequest.TargetInfo> testTargetInfoList = new ArrayList<>();
        testTargetInfoList.add(testTargetInfo1);
        testTargetInfoList.add(testTargetInfo2);

        PostPayCreateRequest testDto = new PostPayCreateRequest(
                20000,
                "우리은행",
                "111-111-111",
                testTargetInfoList
        );

        PayRequest testPayRequest = new PayRequest();
        testPayRequest.savePayRequest(user5, testSpace, testDto.getTotalAmount(), testDto.getBankName(), testDto.getBankAccountNum(), false);

        PayRequestTarget testPayRequestTarget_1 = new PayRequestTarget();
        testPayRequestTarget_1.savePayRequestTarget(testPayRequest, user6.getUserId(), 10000, false);
        PayRequestTarget testPayRequestTarget_2 = new PayRequestTarget();
        testPayRequestTarget_2.savePayRequestTarget(testPayRequest, user7.getUserId(), 10000, false);

        when(userUtils.findUserByUserId(user5.getUserId())).thenReturn(user5);
        when(spaceUtils.findSpaceBySpaceId(testSpace.getSpaceId())).thenReturn(testSpace);
        when(payDao.createPayRequest(user5, testSpace, testDto.getTotalAmount(), testDto.getBankName(), testDto.getBankAccountNum(), false)).thenReturn(testPayRequest);
        when(payDao.createPayRequestTarget(testPayRequest, testTargetInfo1.getTargetUserId(), testTargetInfo1.getRequestAmount(), false)).thenReturn(testPayRequestTarget_1);
        when(payDao.createPayRequestTarget(testPayRequest, testTargetInfo2.getTargetUserId(), testTargetInfo2.getRequestAmount(), false)).thenReturn(testPayRequestTarget_2);

        //when
        List<PayRequestTarget> testPayRequestTargetList = payService.createPay(user5.getUserId(), testSpace.getSpaceId(), testDto);

        //then
        PayRequestTarget result_1 = testPayRequestTargetList.get(0);
        assertThat(result_1.getPayRequest()).isEqualTo(testPayRequest);
        assertThat(result_1.getTargetUserId()).isEqualTo(user6.getUserId());
        assertThat(result_1.getRequestAmount()).isEqualTo(10000);

        PayRequestTarget result_2 = testPayRequestTargetList.get(1);
        assertThat(result_2.getPayRequest()).isEqualTo(testPayRequest);
        assertThat(result_2.getTargetUserId()).isEqualTo(user7.getUserId());
        assertThat(result_2.getRequestAmount()).isEqualTo(10000);
    }

}