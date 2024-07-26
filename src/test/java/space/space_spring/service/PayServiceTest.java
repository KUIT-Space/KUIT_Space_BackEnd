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
    private Space testSpace;
    private PayRequest testPayRequest;
    private PayRequestTarget testPayRequestTarget;

    @BeforeEach
    public void 테스트_셋업() {
        /**
         * user2이 같은 스페이스에 속한 user1에게 정산을 요청한 상황 가정
         */
        user1 = new User();
        user1.saveUser("test1@test.com", "abcDEF123!@", "user1", UserSignupType.LOCAL);

        user2 = new User();
        user2.saveUser("test2@test.com", "abcDEF123!@", "user2", UserSignupType.LOCAL);

        testSpace = new Space();
        testSpace.saveSpace("testSpace", "test_profile_img_url");

        testPayRequest = new PayRequest();
        testPayRequest.savePayRequest(user2, testSpace, 30000, "우리은행", "111-111-111", false);

        testPayRequestTarget = new PayRequestTarget();
        testPayRequestTarget.savePayRequestTarget(testPayRequest, user1.getUserId(), 10000, false);
    }

    @Test
    @DisplayName("getPayRequestInfoForUser_메서드_테스트")
    void 유저가_요청한_정산_중_진행중인_정산리스트_찾는_메서드_테스트() throws Exception {
        //given
        when(userUtils.findUserByUserId(user2.getUserId())).thenReturn(user2);
        when(spaceUtils.findSpaceBySpaceId(testSpace.getSpaceId())).thenReturn(testSpace);
        when(payDao.findPayRequestListByUser(user2, testSpace)).thenReturn(List.of(testPayRequest));
        when(payDao.findPayRequestTargetListByPayRequest(testPayRequest)).thenReturn(List.of(testPayRequestTarget));

        //when
        List<PayRequestInfoDto> payRequestInfoForUser = payService.getPayRequestInfoForUser(user2.getUserId(), testSpace.getSpaceId());

        //then
        assertThat(payRequestInfoForUser.size()).isEqualTo(1);

        for (PayRequestInfoDto payRequestInfoDto : payRequestInfoForUser) {
            assertThat(payRequestInfoDto.getTotalAmount()).isEqualTo(30000);
            assertThat(payRequestInfoDto.getReceiveAmount()).isEqualTo(0);
            assertThat(payRequestInfoDto.getTotalTargetNum()).isEqualTo(1);
            assertThat(payRequestInfoDto.getReceiveTargetNum()).isEqualTo(0);
        }
    }

    @Test
    @DisplayName("Test name")
    void 유저가_요청받은_정산_중_진행중인_정산리스트_찾는_메서드_테스트() throws Exception {
        //given
        when(userUtils.findUserByUserId(user1.getUserId())).thenReturn(user1);
        when(spaceUtils.findSpaceBySpaceId(testSpace.getSpaceId())).thenReturn(testSpace);
        when(payDao.findPayRequestTargetListByUser(user1, testSpace)).thenReturn(List.of(testPayRequestTarget));

        //when
        List<PayReceiveInfoDto> payReceiveInfoForUser = payService.getPayReceiveInfoForUser(user1.getUserId(), testSpace.getSpaceId());

        //then
        assertThat(payReceiveInfoForUser.size()).isEqualTo(1);
        for (PayReceiveInfoDto payReceiveInfoDto : payReceiveInfoForUser) {
            assertThat(payReceiveInfoDto.getPayCreatorName()).isEqualTo(user2.getUserName());
            assertThat(payReceiveInfoDto.getRequestAmount()).isEqualTo(10000);
        }
    }




}