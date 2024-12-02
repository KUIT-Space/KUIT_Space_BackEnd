package space.space_spring.domain.pay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import space.space_spring.argumentResolver.jwtLogin.JwtLoginAuthHandlerArgumentResolver;
import space.space_spring.argumentResolver.userSpace.UserSpaceAuthHandlerArgumentResolver;
import space.space_spring.argumentResolver.userSpace.UserSpaceIdHandlerArgumentResolver;
import space.space_spring.domain.pay.model.request.PayCreateRequest;
import space.space_spring.domain.pay.model.request.TargetInfoRequest;
import space.space_spring.domain.pay.service.PayService;
import space.space_spring.interceptor.UserSpaceValidationInterceptor;
import space.space_spring.interceptor.jwtLogin.JwtLoginAuthInterceptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = PayController.class)
class PayControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PayService payService;      // PayController 의 bean 생성을 위함

    @MockBean
    private JwtLoginAuthInterceptor jwtLoginAuthInterceptor;

    @MockBean
    private UserSpaceValidationInterceptor userSpaceValidationInterceptor;

    @MockBean
    private JwtLoginAuthHandlerArgumentResolver jwtLoginAuthHandlerArgumentResolver;

    @MockBean
    private UserSpaceIdHandlerArgumentResolver userSpaceIdHandlerArgumentResolver;

    @MockBean
    private UserSpaceAuthHandlerArgumentResolver userSpaceAuthHandlerArgumentResolver;

    @BeforeEach
    void setUp() throws Exception {
        // Mock the behavior of interceptors to always return true
        given(jwtLoginAuthInterceptor.preHandle(any(), any(), any())).willReturn(true);
        given(userSpaceValidationInterceptor.preHandle(any(), any(), any())).willReturn(true);

        // Mock the behavior of argument resolvers
        given(jwtLoginAuthHandlerArgumentResolver.supportsParameter(any())).willReturn(true);
        given(jwtLoginAuthHandlerArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(1L); // Mock userId

        given(userSpaceIdHandlerArgumentResolver.supportsParameter(any())).willReturn(true);
        given(userSpaceIdHandlerArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn(1L); // Mock userSpaceId

        given(userSpaceAuthHandlerArgumentResolver.supportsParameter(any())).willReturn(true);
        given(userSpaceAuthHandlerArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .willReturn("SomeAuth"); // Mock auth
    }


    @Test
    @DisplayName("정산 생성자에게 받은 정보에 따라서 정산을 생성한다.")
    @WithMockUser       // 인증된 사용자 요청 가정
    void createPay() throws Exception {
        //given
        TargetInfoRequest targetInfoRequest1 = TargetInfoRequest.builder()
                .targetUserId(1L)
                .requestedAmount(10000)
                .build();
        TargetInfoRequest targetInfoRequest2 = TargetInfoRequest.builder()
                .targetUserId(2L)
                .requestedAmount(20000)
                .build();
        List<TargetInfoRequest> targetInfoRequests = List.of(targetInfoRequest1, targetInfoRequest2);

        PayCreateRequest request = PayCreateRequest.builder()
                .totalAmount(30000)
                .bankName("우리은행")
                .bankAccountNum("111-111")
                .targetInfoRequests(targetInfoRequests)
                .payType("INDIVIDUAL")
                .build();

        //when //then
        mockMvc.perform(
                        post("/space/1/pay")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .with(csrf())       // csrf 토큰 추가
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("1000"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("요청에 성공하였습니다."));

    }

    @Test
    @DisplayName("")
    void test() throws Exception {
        //given

        //when

        //then
    }


}