package space.space_spring.controller;

import jakarta.validation.constraints.NotNull;
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
@RequestMapping("/space/{spaceId}/voiceRoom")
public class VoiceRoomController {
    private final LiveKitService liveKitService;
    private final VoiceRoomService voiceRoomService;

    //VoiceRoom 생성/수정
    @PostMapping("")
    public BaseResponse<Boolean> createRoom(
            @PathVariable("spaceId") @NotNull long spaceId,
            @RequestBody PostVoiceRoomDto.Request voiceRoomRequest){

        //Todo 해당 유저가 voice이 있는 space에 포함되어 있는지(권한이 있는지) 확인

        //Todo 해당 유저가 현재 space에 대해 관리자 권한을 갖고 있는지 확인

        //Todo response 내용을 무엇을 주면 좋을지 ( POST response 전체 기능 통일 하는 것일 좋아보임 )
        return new BaseResponse<Boolean>(voiceRoomService.createVoiceRoom(spaceId,voiceRoomRequest));
    }

    //현재 space에 VoiceRoom 목록
    @GetMapping("")
    public BaseResponse<GetVoiceRoomList.Response> getRoomList(
            @PathVariable("spaceId") @NotNull long spaceId,
            @RequestBody GetVoiceRoomList.Request voiceRoomListList){
        //Todo 해당 유저가, voiceRoom이 있는 space에 포함되어 있는지(권한이 있는지) 확인

        List<GetVoiceRoomList.VoiceRoomInfo> roomInfoList = voiceRoomService.getVoiceRoomInfoList(spaceId,voiceRoomListList);
        return new BaseResponse<GetVoiceRoomList.Response>(new GetVoiceRoomList.Response(roomInfoList));
    }

    //VoiceRoom입장 token 발행
    @GetMapping("/token")
    public BaseResponse<GetToken.Response> getToken(
            @PathVariable("spaceId") @NotNull long spaceId,
            @JwtLoginAuth Long userId,
            @RequestBody GetToken.Request req
    ){
        //Todo 해당 유저가 voice이 있는 space에 포함되어 있는지(권한이 있는지) 확인

        return new BaseResponse<GetToken.Response>(
            new GetToken.Response(voiceRoomService.getToken(spaceId, userId,req.getRoomId() ))
        );
    }

    //VoiceRoom에 참가자 정보
    @GetMapping("/participant")
    public BaseResponse<GetParticipantList.Response> getParticipants(
            @PathVariable("spaceId") @NotNull long spaceId,
            @RequestBody GetParticipantList.Request req
    ){
        //Todo 해당 유저가 voice이 있는 space에 포함되어 있는지(권한이 있는지) 확인
        List<GetParticipantList.ParticipantInfo> participantInfoList = voiceRoomService.getParticipantInfoListById(req.getRoomId());
        return new BaseResponse<GetParticipantList.Response>(new GetParticipantList.Response(participantInfoList));
    }

    //VoiceRoom 변동사항 전달
    @PostMapping("/status")
    public BaseResponse<String> postRoomStatus(){
        //Todo 해당 유저가 voice이 있는 space에 포함되어 있는지(권한이 있는지) 확인
        return new BaseResponse<String>(null);
    }

    private boolean validateSpaceAuth(long spaceId,long userId){
        //Todo 해당 유저가 voice이 있는 space에 포함되어 있는지(권한이 있는지) 확인
        return true;
    }
    private boolean validateVoiceRoom(long voiceRoomId){
        //Todo 해당 보이스룸이 존재하는지 확인
        return true;
    }
    private boolean validateManagerAuth(long spaceId,long userId){
        //Todo 해당 유저가 현재 space에 대해 관리자 권한을 갖고 있는지 확인
        return true;
    }
}
