package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.space_spring.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.dto.chat.request.CreateChatRoomRequest;
import space.space_spring.dto.chat.request.JoinChatRoomRequest;
import space.space_spring.dto.chat.response.ChatSuccessResponse;
import space.space_spring.dto.chat.response.CreateChatRoomResponse;
import space.space_spring.dto.chat.response.ReadChatRoomMemberResponse;
import space.space_spring.dto.chat.response.ReadChatRoomResponse;
import space.space_spring.exception.ChatException;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.ChatRoomService;
import space.space_spring.service.S3Uploader;
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.io.IOException;

import static space.space_spring.response.status.BaseExceptionResponseStatus.*;
import static space.space_spring.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final S3Uploader s3Uploader;
    private final UserSpaceUtils userSpaceUtils;

    /**
     * 모든 채팅방 정보 조회
     */
    @GetMapping("/chatroom")
    public BaseResponse<ReadChatRoomResponse> readChatRooms(@JwtLoginAuth Long userId, @PathVariable Long spaceId) {
        return new BaseResponse<>(chatRoomService.readChatRooms(userId, spaceId));
    }

    /**
     * 채팅방 생성
     */
    @PostMapping("/chatroom")
    public BaseResponse<CreateChatRoomResponse> createChatRoom(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @Validated @ModelAttribute CreateChatRoomRequest createChatRoomRequest,
            BindingResult bindingResult) throws IOException {

        if (!userSpaceUtils.isUserManager(userId, spaceId)) {
            throw new ChatException(UNAUTHORIZED_USER);
        }

        if (bindingResult.hasErrors()) {
            throw new ChatException(INVALID_CHATROOM_CREATE, getErrorMessage(bindingResult));
        }

        String chatRoomDirName = "chatRoomImg";
        String chatRoomImgUrl = s3Uploader.upload(createChatRoomRequest.getImg(), chatRoomDirName);

        return new BaseResponse<>(chatRoomService.createChatRoom(userId, spaceId, createChatRoomRequest, chatRoomImgUrl));
    }

    /**
     * 특정 채팅방의 모든 유저 정보 조회
     */
    @GetMapping("/{chatRoomId}/member")
    public BaseResponse<ReadChatRoomMemberResponse> readChatRoomMembers(@JwtLoginAuth Long userId, @PathVariable Long spaceId, @PathVariable Long chatRoomId) {

        if (!userSpaceUtils.isUserManager(userId, spaceId)) {
            throw new ChatException(UNAUTHORIZED_USER);
        }

        return new BaseResponse<>(chatRoomService.readChatRoomMembers(spaceId, chatRoomId));
    }

    /**
     * 특정 채팅방에 유저 초대
     */
    @PostMapping("/{chatRoomId}/member")
    public BaseResponse<ChatSuccessResponse> joinChatRoom(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long chatRoomId,
            @RequestBody JoinChatRoomRequest joinChatRoomRequest,
            BindingResult bindingResult) {

        if (!userSpaceUtils.isUserManager(userId, spaceId)) {
            throw new ChatException(UNAUTHORIZED_USER);
        }

        if(bindingResult.hasErrors()){
            throw new ChatException(INVALID_CHATROOM_JOIN,getErrorMessage(bindingResult));
        }

        return new BaseResponse<>(chatRoomService.joinChatRoom(chatRoomId, joinChatRoomRequest));
    }

    /**
     * 특정 채팅방의 이름 수정
     */
    @PostMapping("/{chatRoomId}/setting")
    public BaseResponse<ChatSuccessResponse> modifyChatRoomName(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long chatRoomId,
            @RequestParam String name) {

        if (!userSpaceUtils.isUserManager(userId, spaceId)) {
            throw new ChatException(UNAUTHORIZED_USER);
        }

        return new BaseResponse<>(chatRoomService.modifyChatRoomName(chatRoomId, name));
    }

    /**
     * 특정 채팅방에서 나가기
     */
    @PostMapping("/{chatRoomId}/exit")
    public BaseResponse<ChatSuccessResponse> exitChatRoom(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long chatRoomId) {
        return new BaseResponse<>(chatRoomService.exitChatRoom(userId, chatRoomId));
    }

    /**
     * 특정 채팅방 삭제
     */
    @PostMapping("/{chatRoomId}/delete")
    public BaseResponse<ChatSuccessResponse> deleteChatRoom(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @PathVariable Long chatRoomId) {

        if (!userSpaceUtils.isUserManager(userId, spaceId)) {
            throw new ChatException(UNAUTHORIZED_USER);
        }

        return new BaseResponse<>(chatRoomService.deleteChatRoom(chatRoomId));
    }
}
