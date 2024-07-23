package space.space_spring.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import space.space_spring.dao.PayDao;
import space.space_spring.entity.PayRequest;
import space.space_spring.entity.PayRequestTarget;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.util.user.UserUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PayServiceTest {

    @InjectMocks
    private PayService payService;

    @Mock
    private PayDao payDao;

    @Mock
    private UserUtils userUtils;

    @BeforeEach
    public void 테스트_셋업() {
        User user1 = new User();
        user1.saveUser("test1@test.com", "abcDEF123!@", "user1");

        User user2 = new User();
        user2.saveUser("test2@test.com", "abcDEF123!@", "user2");

        Space testSpace = new Space();
        testSpace.saveSpace("testSpace", "test_profile_img_url");

        // user2가 요청한 정산 -> user1 에게
        PayRequest testPayRequest = new PayRequest();
        testPayRequest.savePayRequest(user2, testSpace, 30000, "우리은행", "111-111-111", false);

        PayRequestTarget testPayRequestTarget = new PayRequestTarget();
        testPayRequestTarget.savePayRequestTarget(testPayRequest, user1.getUserId(), 10000, false);
    }

    @Test
    @DisplayName("getPayRequestInfoForUser_메서드_테스트")
    void getPayRequestInfoForUser_메서드_테스트() throws Exception {
        //given
        

        //when
        
        //then
    }
    
}