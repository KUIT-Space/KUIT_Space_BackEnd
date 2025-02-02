//package space.space_spring.domain.pay.adapter.in.web.createPay;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.SpringBootWebTestClientBuilderCustomizer;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import space.space_spring.domain.pay.application.port.in.createPay.CreatePayCommand;
//import space.space_spring.domain.pay.application.port.in.createPay.CreatePayUseCase;
//import space.space_spring.global.interceptor.jwtLogin.JwtLoginAuthInterceptor;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(controllers = CreatePayController.class)
//class CreatePayControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CreatePayUseCase createPayUseCase;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//
//    @Test
//    @DisplayName("")
//    void createPay() throws Exception {
//        //given
//        RequestOfCreatePay request = new RequestOfCreatePay(
//                10000,
//                "우리은행",
//                "123-123",
//                List.of(
//                        new TargetOfPayRequest(2L, 3333),
//                        new TargetOfPayRequest(3L, 3333),
//                        new TargetOfPayRequest(4L, 3333)
//                ),
//                "EQUAL_SPLIT"
//        );
//
//        //when //then
//        mockMvc.perform(
//                        post("/pay/create")
//                                .header("")         // 인터셉터에서 가져오는 id값 어떻게 처리 ?? -> 인터셉터 구현 후 컨트롤러 테스트 다시 작성
//                                .content(objectMapper.writeValueAsString(request))
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.code").value("200"))
//                .andExpect(jsonPath("$.status").value("OK"))
//                .andExpect(jsonPath("$.message").value("OK"));
//    }
//
//}