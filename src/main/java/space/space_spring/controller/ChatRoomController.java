package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import space.space_spring.argument_resolver.jwtLogin.JwtLoginAuth;
import space.space_spring.dto.chat.ReadChatRoomResponse;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.ChatRoomService;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/chatroom")
public class ChatRoomController {
    private final ChatRoomService chatRoomService;

    @GetMapping("/space/{spaceId}/chatroom")
    public BaseResponse<ReadChatRoomResponse> readChatRooms(@JwtLoginAuth Long userId, @PathVariable Long spaceId) {
        return new BaseResponse<>(chatRoomService.readChatRooms(userId, spaceId));
    }
}
