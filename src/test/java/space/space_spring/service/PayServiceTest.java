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
import space.space_spring.entity.PayRequest;
import space.space_spring.entity.PayRequestTarget;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.enumStatus.UserSignupType;
import space.space_spring.util.space.SpaceUtils;
import space.space_spring.util.user.UserUtils;

import java.util.List;

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
    private Space testSpace;
    private PayRequest testPayRequest;
    private PayRequestTarget testPayRequestTarget_1;
    private PayRequestTarget testPayRequestTarget_2;
    private PayRequestTarget testPayRequestTarget_3;

    @BeforeEach
    public void 테스트_셋업() {
        /**
         * user1이 같은 스페이스에 속한 user2, 3, 4 에게 정산을 요청한 상황 가정
         * user 2, 3은 아직 정산 진행 중
         * user 4 는 정산 완료
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

        testPayRequest = new PayRequest();
        testPayRequest.savePayRequest(user1, testSpace, 30000, "우리은행", "111-111-111", false);

        testPayRequestTarget_1 = new PayRequestTarget();
        testPayRequestTarget_1.savePayRequestTarget(testPayRequest, user2.getUserId(), 10000, false);

        testPayRequestTarget_2 = new PayRequestTarget();
        testPayRequestTarget_2.savePayRequestTarget(testPayRequest, user3.getUserId(), 10000, false);

        testPayRequestTarget_3 = new PayRequestTarget();
        testPayRequestTarget_3.savePayRequestTarget(testPayRequest, user4.getUserId(), 10000, true);

    }

    @Test
    @DisplayName("user1이_testSpace에서_요청한_정산중_현재진행중인_정산리스트_찾기")
    void user1이_testSpace에서_요청한_정산중_현재진행중인_정산리스트_찾기() throws Exception {
        //given
        when(userUtils.findUserByUserId(user1.getUserId())).thenReturn(user1);
        when(spaceUtils.findSpaceBySpaceId(testSpace.getSpaceId())).thenReturn(testSpace);
        when(payDao.findPayRequestListByUser(user1, testSpace, false)).thenReturn(List.of(testPayRequest));
        when(payDao.findPayRequestTargetListByPayRequest(testPayRequest)).thenReturn(List.of(testPayRequestTarget_1, testPayRequestTarget_2, testPayRequestTarget_3));

        //when
        List<PayRequestInfoDto> payRequestInfoForUser = payService.getPayRequestInfoForUser(user2.getUserId(), testSpace.getSpaceId(), false);

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
    @DisplayName("user2가_testSpace에서_요청받은_정산중_현재진행중인_정산리스트_찾기")
    void user2가_testSpace에서_요청받은_정산중_현재진행중인_정산리스트_찾기() throws Exception {
        //given
        when(userUtils.findUserByUserId(user2.getUserId())).thenReturn(user2);
        when(spaceUtils.findSpaceBySpaceId(testSpace.getSpaceId())).thenReturn(testSpace);
        when(payDao.findPayRequestTargetListByUser(user2, testSpace, false)).thenReturn(List.of(testPayRequestTarget_1));

        //when
        List<PayReceiveInfoDto> payReceiveInfoForUser = payService.getPayReceiveInfoForUser(user1.getUserId(), testSpace.getSpaceId(), false);

        //then
        assertThat(payReceiveInfoForUser.size()).isEqualTo(1);
        for (PayReceiveInfoDto payReceiveInfoDto : payReceiveInfoForUser) {
            assertThat(payReceiveInfoDto.getPayCreatorName()).isEqualTo(user1.getUserName());
            assertThat(payReceiveInfoDto.getRequestAmount()).isEqualTo(10000);
        }
    }

    @Test
    @DisplayName("user2가_testSpace에서_요청한_정산중_완료된_정산리스트_찾기")
    void user2가_TestSpace에서_요청한_정산중_완료된_정산리스트_찾기() throws Exception {
        //given
        /**
         * user2 가 testSpace에서 user1에게 정산을 요청 & 이 정산은 완료된 상황을 가정
         */
        PayRequest testPayRequest = new PayRequest();
        testPayRequest.savePayRequest(user2, testSpace, 10000, "우리은행", "111-111-111", true);
        PayRequestTarget testPayRequestTarget = new PayRequestTarget();
        testPayRequestTarget.savePayRequestTarget(testPayRequest, user1.getUserId(), 10000, true);

        when(userUtils.findUserByUserId(user2.getUserId())).thenReturn(user2);
        when(spaceUtils.findSpaceBySpaceId(testSpace.getSpaceId())).thenReturn(testSpace);
        when(payDao.findPayRequestListByUser(user2, testSpace, true)).thenReturn(List.of(testPayRequest));
        when(payDao.findPayRequestTargetListByPayRequest(testPayRequest)).thenReturn(List.of(testPayRequestTarget));

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


}