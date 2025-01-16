package space.space_spring.domain.voiceRoom.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.space_spring.global.argumentResolver.jwtLogin.JwtLoginAuth;
import space.space_spring.global.argumentResolver.userSpace.UserSpaceAuth;
import space.space_spring.global.argumentResolver.userSpace.UserSpaceId;
import space.space_spring.domain.userSpace.repository.UserSpaceDao;
import space.space_spring.domain.voiceRoom.repository.VoiceRoomRepository;
import space.space_spring.domain.voiceRoom.model.dto.GetParticipantList;
import space.space_spring.domain.voiceRoom.model.dto.GetVoiceRoomList;
import space.space_spring.domain.voiceRoom.model.dto.PatchVoiceRoom;
import space.space_spring.domain.voiceRoom.model.dto.PostVoiceRoomDto;

import space.space_spring.global.exception.CustomException;
import space.space_spring.global.common.response.BaseResponse;
import space.space_spring.domain.voiceRoom.service.LiveKitService;
import space.space_spring.domain.voiceRoom.service.VoiceRoomParticipantService;
import space.space_spring.domain.voiceRoom.service.VoiceRoomService;
import space.space_spring.global.util.space.SpaceUtils;
import space.space_spring.global.util.user.UserUtils;
import space.space_spring.global.util.userSpace.UserSpaceUtils;
import static space.space_spring.global.common.response.status.BaseExceptionResponseStatus.*;

import java.util.List;

import static space.space_spring.global.common.enumStatus.UserSpaceAuth.MANAGER;
import static space.space_spring.global.util.bindingResult.BindingResultUtils.getErrorMessage;

@RestController
@RequiredArgsConstructor
@RequestMapping("/space/{spaceId}/voiceRoom")
public class VoiceRoomController {
    private final LiveKitService liveKitService;
    private final VoiceRoomService voiceRoomService;
    private final UserSpaceUtils userSpaceUtils;
    private final VoiceRoomRepository voiceRoomRepository;
    private final UserSpaceDao userSpaceDao;
    private final UserUtils userUtils;
    private final SpaceUtils spaceUtils;
    private final VoiceRoomParticipantService voiceRoomParticipantService;

    //VoiceRoom 생성/수정
    @PostMapping("")
    public BaseResponse<PostVoiceRoomDto.Response> createRoom(
            @PathVariable("spaceId") @NotNull long spaceId,
            @JwtLoginAuth Long userId,
            @Validated @RequestBody PostVoiceRoomDto.Request voiceRoomRequest,
            @UserSpaceAuth String userSpaceAuth,
            BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new CustomException(INVALID_VOICEROOM_REQUEST,getErrorMessage(bindingResult));
        }

        //해당 유저가 현재 space에 대해 관리자 권한을 갖고 있는지 확인
        validateManagerPermission(userSpaceAuth);

        //Todo response 내용을 무엇을 주면 좋을지 ( POST response 전체 기능 통일 하는 것일 좋아보임 )
        PostVoiceRoomDto.Response res = new PostVoiceRoomDto.Response(voiceRoomService.createVoiceRoom(spaceId,voiceRoomRequest));
        return new BaseResponse<>(res);
    }

    //현재 space에 VoiceRoom 목록
    @GetMapping("")
    public BaseResponse<GetVoiceRoomList.Response> getRoomList(
            @PathVariable("spaceId") @NotNull long spaceId,
            @JwtLoginAuth Long userId,
            //@RequestBody GetVoiceRoomList.Request voiceRoomList,
            @RequestParam(required = false,defaultValue = "0") Integer limit,
            @RequestParam(required = false,defaultValue = "false") Boolean showParticipant){

        boolean showParticipantValue = (showParticipant != null) ? showParticipant : false;


        GetVoiceRoomList.Request voiceRoomList=new GetVoiceRoomList.Request(limit, showParticipant);

        List<GetVoiceRoomList.VoiceRoomInfo> roomInfoList = voiceRoomService.getVoiceRoomInfoListConcurrency(spaceId,voiceRoomList);
        return new BaseResponse<GetVoiceRoomList.Response>(new GetVoiceRoomList.Response(roomInfoList));
    }

    @GetMapping("/con/{version}")
    public BaseResponse<GetVoiceRoomList.Response> getRoomListNonConCurrent(
            @PathVariable("spaceId") @NotNull long spaceId,
            @JwtLoginAuth Long userId,
            //@RequestBody GetVoiceRoomList.Request voiceRoomList,
            @RequestParam(required = false,defaultValue = "0") Integer limit,
            @RequestParam(required = false,defaultValue = "false") Boolean showParticipant
            ){

        boolean showParticipantValue = (showParticipant != null) ? showParticipant : false;


        GetVoiceRoomList.Request voiceRoomList=new GetVoiceRoomList.Request(limit, showParticipant);
        List<GetVoiceRoomList.VoiceRoomInfo> roomInfoList;

        roomInfoList= voiceRoomService.getVoiceRoomInfoListConcurrency(spaceId,voiceRoomList);

        return new BaseResponse<GetVoiceRoomList.Response>(new GetVoiceRoomList.Response(roomInfoList));
    }

    //VoiceRoom입장 token 발행
    @GetMapping("/{voiceRoomId}/token")
    public BaseResponse<String> getToken(
            @PathVariable("spaceId") @NotNull long spaceId,
            @JwtLoginAuth Long userId,
            @PathVariable("voiceRoomId") @NotNull Long roomId,
            @UserSpaceId Long userSpaceId,
            HttpServletResponse response
    ){

        //해당 voiceRoomId가 존재하는지 확인
        validateVoiceRoom(roomId);
        //해당 voiceRoom이 해당 space에 속한것이 맞는지 확인
        validateVoiceRoomInSpace(spaceId,roomId);

        response.setHeader("Authorization", "Bearer " + voiceRoomService.getToken(spaceId, userId,userSpaceId,roomId));
        return new BaseResponse<String>(
            "보이스룸 토큰 생성에 성공했습니다."
        );
    }

    //VoiceRoom에 참가자 정보
    @GetMapping("/{voiceRoomId}/participant")
    public BaseResponse<GetParticipantList.Response> getParticipants(
            @PathVariable("spaceId") @NotNull long spaceId,
            @JwtLoginAuth Long userId,
            @PathVariable("voiceRoomId") @NotNull Long roomId
    ){

        //해당 voiceRoomId가 존재하는지 확인
        validateVoiceRoom(roomId);
        //해당 voiceRoom이 해당 space에 속한것이 맞는지 확인
        validateVoiceRoomInSpace(spaceId,roomId);

        //List<GetParticipantList.ParticipantInfo> participantInfoList = voiceRoomService.getParticipantInfoListById(roomId);
        List<GetParticipantList.ParticipantInfo> participantInfoList = voiceRoomParticipantService.getParticipantInfoListById(roomId);

        return new BaseResponse<GetParticipantList.Response>(new GetParticipantList.Response(participantInfoList));
    }
    @PatchMapping("")
    public BaseResponse<String> updateVoiceRoom(
            @PathVariable("spaceId") @NotNull long spaceId,
            @JwtLoginAuth Long userId,
            @Validated @RequestBody PatchVoiceRoom patchVoiceRoom,
            @UserSpaceAuth String userSpaceAuth,
            BindingResult bindingResult
    ){

        if(bindingResult.hasErrors()){
            throw new CustomException(INVALID_VOICEROOM_REQUEST,getErrorMessage(bindingResult));
        }
        //해당 유저가 현재 space에 대해 관리자 권한을 갖고 있는지 확인
        validateManagerPermission(userSpaceAuth);

        for(PatchVoiceRoom.UpdateRoom updateRoom : patchVoiceRoom.getUpdateRoomList()) {
            //해당 voiceRoomId가 존재하는지 확인
            validateVoiceRoom(updateRoom.getRoomId());
            //해당 voiceRoom이 해당 space에 속한것이 맞는지 확인
            validateVoiceRoomInSpace(spaceId, updateRoom.getRoomId());
        }

        voiceRoomService.updateVoiceRoom(patchVoiceRoom.getUpdateRoomList());

        return new BaseResponse<>("success");
    }

    @DeleteMapping("/{voiceRoomId}")
    public BaseResponse<String> deleteVoiceRoom(
            @PathVariable("spaceId") @NotNull long spaceId,
            @JwtLoginAuth Long userId,
            @PathVariable("voiceRoomId") @NotNull Long voiceRoomId,
            @UserSpaceAuth String userSpaceAuth
    ){

        //해당 유저가 현재 space에 대해 관리자 권한을 갖고 있는지 확인
        validateManagerPermission(userSpaceAuth);
        //해당 보이스룸이 존재하는지 확인
        validateVoiceRoom(voiceRoomId);
        //해당 voiceRoom이 해당 space에 속한것이 맞는지 확인
        validateVoiceRoomInSpace(spaceId, voiceRoomId);

        voiceRoomService.deleteVoiceRoom(voiceRoomId);
        return new BaseResponse<>("success");
    }

    //VoiceRoom 변동사항 전달
    @PostMapping("/status")
    public BaseResponse<String> postRoomStatus(){
        //Todo 해당 유저가 voice이 있는 space에 포함되어 있는지(권한이 있는지) 확인
        return new BaseResponse<String>(null);
    }

    private boolean validateVoiceRoom(long voiceRoomId){
        //Todo 해당 보이스룸이 존재하는지 확인
        if(!voiceRoomRepository.existsByVoiceRoomId(voiceRoomId)){
            throw new CustomException(VOICEROOM_NOT_EXIST);
        }
        return true;
    }
    private boolean validateVoiceRoomNameExist(String voiceRoomName){
        if(!voiceRoomRepository.existsByName(voiceRoomName)){
            throw new CustomException(VOICEROOM_NAME_ALREADY_EXIST);
        }
        return true;
    }
    private boolean validateVoiceRoomInSpace(long spaceId,long voiceRoomId){
        if(! (voiceRoomRepository.findById(voiceRoomId).orElseThrow(()->new CustomException(VOICEROOM_NOT_EXIST))
                .getSpace().getSpaceId().equals(spaceId))){
            throw new CustomException(VOICEROOM_NOT_IN_SPACE);
        }
        return true;
    }
    private boolean validateManagerPermission(String userSpaceAuth){
        //해당 유저가 현재 space에 대해 관리자 권한을 갖고 있는지 확인
        if(!userSpaceAuth.equals(MANAGER.getAuth())){
            throw new CustomException(UNAUTHORIZED_USER);
        }
        return true;
    }
}
