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
import space.space_spring.exception.SpaceException;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.ChatRoomService;

import static space.space_spring.response.status.BaseExceptionResponseStatus.INVALID_CHATROOM_CREATE;
import static space.space_spring.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/chat")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/chatroom")
    public BaseResponse<ReadChatRoomResponse> readChatRooms(@JwtLoginAuth Long userId, @PathVariable Long spaceId) {
        return new BaseResponse<>(chatRoomService.readChatRooms(userId, spaceId));
    }

    @PostMapping("/chatroom")
    public BaseResponse<CreateChatRoomResponse> createChatRoom(
            @JwtLoginAuth Long userId,
            @PathVariable Long spaceId,
            @Validated @RequestBody CreateChatRoomRequest createChatRoomRequest,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ChatException(INVALID_CHATROOM_CREATE, getErrorMessage(bindingResult));
        }

        return new BaseResponse<>(chatRoomService.createChatRoom(userId, spaceId, createChatRoomRequest));
    }
}
