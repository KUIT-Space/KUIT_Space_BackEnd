package space.space_spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import space.space_spring.argument_resolver.jwtLogin.JwtLoginAuth;
import space.space_spring.dto.VoiceRoom.*;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.LiveKitService;
import space.space_spring.service.VoiceRoomService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voiceRoom")
public class VoiceRoomController {
    private final LiveKitService liveKitService;
    private final VoiceRoomService voiceRoomService;

    //VoiceRoom 생성/수정
    @PostMapping("")
    public BaseResponse<Boolean> createRoom(@RequestBody PostVoiceRoomDto.Request voiceRoomRequest){
        return new BaseResponse<Boolean>(voiceRoomService.createVoiceRoom(voiceRoomRequest));
    }

    //현재 space에 VoiceRoom 목록
    @GetMapping("")
    public BaseResponse<GetVoiceRoomList.Response> getRoomList(@RequestBody GetVoiceRoomList.Request voiceRoomListList){
        List<GetVoiceRoomList.VoiceRoomInfo> roomInfoList = voiceRoomService.getVoiceRoomInfoList(voiceRoomListList);
        return new BaseResponse<GetVoiceRoomList.Response>(new GetVoiceRoomList.Response(roomInfoList));
    }

    //VoiceRoom입장 token 발행
    @GetMapping("/token")
    public BaseResponse<GetToken.Response> getToken(@JwtLoginAuth Long userId, @RequestBody GetToken.Request req){
        return new BaseResponse<GetToken.Response>(
            new GetToken.Response(voiceRoomService.getToken(userId,req.getRoomId() ))
        );
    }

    //VoiceRoom에 참가자 정보
    @GetMapping("/participant")
    public BaseResponse<GetParticipantList.Response> getParticipants(@RequestBody GetParticipantList.Request req){
        List<GetParticipantList.ParticipantInfo> participantInfoList = voiceRoomService.getParticipantInfoListById(req.getRoomId());
        return new BaseResponse<GetParticipantList.Response>(new GetParticipantList.Response(participantInfoList));
    }

    //VoiceRoom 변동사항 전달
    @PostMapping("/status")
    public BaseResponse<String> postRoomStatus(){
        return new BaseResponse<String>(null);
    }
}
