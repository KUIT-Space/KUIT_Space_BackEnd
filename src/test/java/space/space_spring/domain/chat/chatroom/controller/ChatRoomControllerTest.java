//package space.space_spring.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.web.servlet.MockMvc;
//import space.space_spring.argumentResolver.jwtLogin.JwtLoginAuthHandlerArgumentResolver;
//import space.space_spring.argumentResolver.userSpace.UserSpaceAuthHandlerArgumentResolver;
//import space.space_spring.argumentResolver.userSpace.UserSpaceIdHandlerArgumentResolver;
//import space.space_spring.config.SecurityConfig;
//import space.space_spring.domain.chat.chatroom.controller.ChatRoomController;
//import space.space_spring.domain.chat.chatroom.model.request.CreateChatRoomRequest;
//import space.space_spring.domain.chat.chatroom.model.request.JoinChatRoomRequest;
//import space.space_spring.domain.chat.chatroom.model.response.ChatRoomResponse;
//import space.space_spring.domain.chat.chatroom.model.response.ChatSuccessResponse;
//import space.space_spring.domain.chat.chatroom.model.response.CreateChatRoomResponse;
//import space.space_spring.domain.chat.chatroom.model.response.ReadChatRoomMemberResponse;
//import space.space_spring.domain.chat.chatroom.model.response.ReadChatRoomResponse;
//import space.space_spring.dto.userSpace.UserInfoInSpace;
//import space.space_spring.interceptor.UserSpaceValidationInterceptor;
//import space.space_spring.interceptor.jwtLogin.JwtLoginAuthInterceptor;
//import space.space_spring.domain.chat.chatroom.service.component.ChatRoomService;
//import space.space_spring.service.S3Uploader;
//
//import java.nio.charset.StandardCharsets;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ChatRoomController.class)
//@MockBean(JpaMetamodelMappingContext.class)
//@Import(SecurityConfig.class)
//@AutoConfigureMockMvc(addFilters = false) // security config ignore
//public class ChatRoomControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private ChatRoomService chatRoomService;
//
//    @MockBean
//    private S3Uploader s3Uploader;
//
//    @MockBean
//    private JwtLoginAuthHandlerArgumentResolver jwtLoginAuthHandlerArgumentResolver;
//
//    @MockBean
//    private UserSpaceIdHandlerArgumentResolver userSpaceIdHandlerArgumentResolver;
//
//    @MockBean
//    private UserSpaceAuthHandlerArgumentResolver userSpaceAuthHandlerArgumentResolver;
//
//    @MockBean
//    private JwtLoginAuthInterceptor jwtLoginAuthInterceptor;
//
//    @MockBean
//    private UserSpaceValidationInterceptor userSpaceValidationInterceptor;
//
//    private static Long userId;
//
//    private static Long spaceId;
//
//    private static Long chatRoomId;
//
//    private static Long userSpaceId;
//
//    private static MockMultipartFile mockImgFile;
//
//    private ChatSuccessResponse commonResponse;
//
//    @BeforeEach
//    void 채팅방_테스트_설정() throws Exception {
//        userId = 0L;
//        spaceId = 1L;
//        userSpaceId = 2L;
//        chatRoomId = 3L;
//
//        given(userSpaceValidationInterceptor.preHandle(any(), any(), any()))
//                .willReturn(true);
//
//        given(jwtLoginAuthInterceptor.preHandle(any(), any(), any()))
//                .willReturn(true);
//
//        given(jwtLoginAuthHandlerArgumentResolver.resolveArgument(any(), any(), any(), any()))
//                .willReturn(userId);
//
//        given(userSpaceIdHandlerArgumentResolver.resolveArgument(any(), any(), any(), any()))
//                .willReturn(userSpaceId);
//
//        given(userSpaceAuthHandlerArgumentResolver.resolveArgument(any(), any(), any(), any()))
//                .willReturn("manager");
//
//        mockImgFile = new MockMultipartFile("img", "test.png", "png", "test file".getBytes(StandardCharsets.UTF_8));
//
//        commonResponse = ChatSuccessResponse.builder().build();
//    }
//
//    @Test
//    @DisplayName("모든_채팅방_조회_테스트")
//    void 모든_채팅방_조회_테스트() throws Exception {
//        // given
//        ReadChatRoomResponse response = ReadChatRoomResponse.of(List.of(new ChatRoomResponse[]{}));
//        given(chatRoomService.readChatRooms(userId, spaceId))
//                .willReturn(response);
//
//        // when & then
//        mockMvc.perform(get("/space/{spaceId}/chat/chatroom", spaceId))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").exists());
//    }
//
//    @Test
//    @DisplayName("채팅방_생성_테스트")
//    void 채팅방_생성_테스트() throws Exception {
//        // given
////        CreateChatRoomRequest request = new CreateChatRoomRequest();
//        CreateChatRoomRequest request = CreateChatRoomRequest.builder()
//                .name("testChatRoom")
//                .img(mockImgFile)
//                .memberList(List.of(4L))
//                .build();
////        request.setName("testChatRoom");
////        request.setImg(mockImgFile);
////        request.setMemberList(List.of(4L));
//
//        CreateChatRoomResponse response = CreateChatRoomResponse.of(chatRoomId);
//        given(chatRoomService.createChatRoom(userId, spaceId, request, "asdf"))
//                .willReturn(response);
//
//        // when & then
//        mockMvc.perform(multipart("/space/{spaceId}/chat/chatroom", spaceId)
//                        .file(mockImgFile)
//                        .param("name", request.getName())
//                        .param("memberList", request.getMemberList().stream()
//                                .map(String::valueOf)
//                                .toArray(String[]::new))
//                        .param("userSpaceAuth", "manager")
//                        .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").exists());
//    }
//
//    @Test
//    @DisplayName("특정_채팅방의_모든_유저_정보_조회_테스트")
//    void 특정_채팅방의_모든_유저_정보_조회_테스트() throws Exception {
//        // given
//        ReadChatRoomMemberResponse response = ReadChatRoomMemberResponse.of(List.of(new UserInfoInSpace[]{}));
//        given(chatRoomService.readChatRoomMembers(userId, spaceId))
//                .willReturn(response);
//
//        // when & then
//        mockMvc.perform(get("/space/{spaceId}/chat/{chatRoomId}/member", spaceId, chatRoomId))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").exists());
//    }
//
//    @Test
//    @DisplayName("특정_채팅방에_유저_초대_테스트")
//    void 특정_채팅방에_유저_초대_테스트() throws Exception {
//        // given
//        JoinChatRoomRequest request = JoinChatRoomRequest.builder().memberList(List.of(1L)).build();
//        String content = objectMapper.writeValueAsString(request);
//
//        given(chatRoomService.joinChatRoom(chatRoomId, request))
//                .willReturn(commonResponse);
//
//        // when & then
//        mockMvc.perform(post("/space/{spaceId}/chat/{chatRoomId}/member", spaceId, chatRoomId)
//                                .param("userSpaceAuth", "manager")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(content))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").exists());
//    }
//
//    @Test
//    @DisplayName("특정_유저가_채팅방에서_떠난_시간_저장_테스트")
//    void 특정_유저가_채팅방에서_떠난_시간_저장_테스트() throws Exception {
//        // given
//        given(chatRoomService.updateLastReadTime(userId, chatRoomId))
//                .willReturn(commonResponse);
//
//        // when & then
//        mockMvc.perform(post("/space/{spaceId}/chat/{chatRoomId}/leave", spaceId, chatRoomId))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").exists());
//    }
//
//    @Test
//    @DisplayName("특정_채팅방의_이름_수정_테스트")
//    void 특정_채팅방의_이름_수정_테스트() throws Exception {
//        // given
//        given(chatRoomService.modifyChatRoomName(chatRoomId, "newChatRoom"))
//                .willReturn(commonResponse);
//
//        // when & then
//        mockMvc.perform(post("/space/{spaceId}/chat/{chatRoomId}/setting", spaceId, chatRoomId)
//                        .param("name", "newChatRoom")
//                        .param("userSpaceAuth", "manager"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").exists());
//    }
//
//    @Test
//    @DisplayName("특정_채팅방에서_나가기_테스트")
//    void 특정_채팅방에서_나가기_테스트() throws Exception {
//        // given
//        given(chatRoomService.exitChatRoom(userId, chatRoomId))
//                .willReturn(commonResponse);
//
//        // when & then
//        mockMvc.perform(post("/space/{spaceId}/chat/{chatRoomId}/exit", spaceId, chatRoomId))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").exists());
//    }
//
//    @Test
//    @DisplayName("특정_채팅방_삭제_테스트")
//    void 특정_채팅방_삭제_테스트() throws Exception {
//        // given
//        given(chatRoomService.deleteChatRoom(chatRoomId))
//                .willReturn(commonResponse);
//
//        // when & then
//        mockMvc.perform(post("/space/{spaceId}/chat/{chatRoomId}/delete", spaceId, chatRoomId)
//                        .param("userSpaceAuth", "manager"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").exists());
//    }
//}
