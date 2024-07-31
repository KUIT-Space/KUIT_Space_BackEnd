package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.space_spring.argument_resolver.jwtLogin.JwtLoginAuth;
import space.space_spring.dto.chat.CreateChatRoomRequest;
import space.space_spring.dto.chat.CreateChatRoomResponse;
import space.space_spring.dto.chat.ReadChatRoomResponse;
import space.space_spring.exception.ChatException;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.ChatRoomService;
import space.space_spring.service.S3Uploader;
import space.space_spring.util.userSpace.UserSpaceUtils;

import java.io.IOException;

import static space.space_spring.response.status.BaseExceptionResponseStatus.INVALID_CHATROOM_CREATE;
import static space.space_spring.response.status.BaseExceptionResponseStatus.UNAUTHORIZED_USER;
import static space.space_spring.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final S3Uploader s3Uploader;
    private final UserSpaceUtils userSpaceUtils;

    @GetMapping("/chatroom")
    public BaseResponse<ReadChatRoomResponse> readChatRooms(@JwtLoginAuth Long userId, @PathVariable Long spaceId) {
        return new BaseResponse<>(chatRoomService.readChatRooms(userId, spaceId));
    }

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
}
