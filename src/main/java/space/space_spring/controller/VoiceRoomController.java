package space.space_spring.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import space.space_spring.argument_resolver.jwtLogin.JwtLoginAuth;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dao.VoiceRoomRepository;
import space.space_spring.dto.VoiceRoom.*;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;
import space.space_spring.exception.VoiceRoomException;
import space.space_spring.response.BaseResponse;
import space.space_spring.service.LiveKitService;
import space.space_spring.service.VoiceRoomService;
import space.space_spring.util.space.SpaceUtils;
import space.space_spring.util.user.UserUtils;
import space.space_spring.util.userSpace.UserSpaceUtils;

import javax.print.DocFlavor;
import java.util.List;
import java.util.Optional;

import static space.space_spring.entity.enumStatus.UserSpaceAuth.MANAGER;
import static space.space_spring.response.status.BaseExceptionResponseStatus.*;
import static space.space_spring.util.bindingResult.BindingResultUtils.getErrorMessage;

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

    //VoiceRoom 생성/수정
    @PostMapping("")
    public BaseResponse<PostVoiceRoomDto.Response> createRoom(
            @PathVariable("spaceId") @NotNull long spaceId,
            @JwtLoginAuth Long userId,
            @Validated @RequestBody PostVoiceRoomDto.Request voiceRoomRequest,
            BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new VoiceRoomException(INVALID_VOICEROOM_REQUEST,getErrorMessage(bindingResult));
        }

        //해당 유저가 voice이 있는 space에 포함되어 있는지(권한이 있는지) 확인
        validateIsUserInSpace(spaceId,userId);
        //해당 유저가 현재 space에 대해 관리자 권한을 갖고 있는지 확인
        validateManagerPermission(spaceId,userId);
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

        //해당 유저가, voiceRoom이 있는 space에 포함되어 있는지(권한이 있는지) 확인
        validateIsUserInSpace(spaceId,userId);

        GetVoiceRoomList.Request voiceRoomList=new GetVoiceRoomList.Request(limit, showParticipant);

        List<GetVoiceRoomList.VoiceRoomInfo> roomInfoList = voiceRoomService.getVoiceRoomInfoList(spaceId,voiceRoomList);
        return new BaseResponse<GetVoiceRoomList.Response>(new GetVoiceRoomList.Response(roomInfoList));
    }

    //VoiceRoom입장 token 발행
    @GetMapping("/{voiceRoomId}/token")
    public BaseResponse<String> getToken(
            @PathVariable("spaceId") @NotNull long spaceId,
            @JwtLoginAuth Long userId,
            @PathVariable("voiceRoomId") @NotNull Long roomId,
            HttpServletResponse response
    ){
        //해당 유저가, voiceRoom이 있는 space에 포함되어 있는지(권한이 있는지) 확인
        validateIsUserInSpace(spaceId,userId);
        //해당 voiceRoomId가 존재하는지 확인
        validateVoiceRoom(roomId);
        //해당 voiceRoom이 해당 space에 속한것이 맞는지 확인
        validateVoiceRoomInSpace(spaceId,roomId);

        response.setHeader("Authorization", "Bearer " + voiceRoomService.getToken(spaceId, userId,roomId));
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
        //해당 유저가 voice이 있는 space에 포함되어 있는지(권한이 있는지) 확인
        validateIsUserInSpace(spaceId,userId);
        //해당 voiceRoomId가 존재하는지 확인
        validateVoiceRoom(roomId);
        //해당 voiceRoom이 해당 space에 속한것이 맞는지 확인
        validateVoiceRoomInSpace(spaceId,roomId);

        List<GetParticipantList.ParticipantInfo> participantInfoList = voiceRoomService.getParticipantInfoListById(roomId);
        return new BaseResponse<GetParticipantList.Response>(new GetParticipantList.Response(participantInfoList));
    }

    @PatchMapping("")
    public BaseResponse<String> updateVoiceRoom(
            @PathVariable("spaceId") @NotNull long spaceId,
            @JwtLoginAuth Long userId,
            @Validated @RequestBody PatchVoiceRoom patchVoiceRoom,
            BindingResult bindingResult
    ){

        if(bindingResult.hasErrors()){
            throw new VoiceRoomException(INVALID_VOICEROOM_REQUEST,getErrorMessage(bindingResult));
        }
        //해당 유저가 voice이 있는 space에 포함되어 있는지(권한이 있는지) 확인
        validateIsUserInSpace(spaceId,userId);
        //해당 유저가 현재 space에 대해 관리자 권한을 갖고 있는지 확인
        validateManagerPermission(spaceId,userId);
        //해당 voiceRoom이 해당 space에 속한것이 맞는지 확인
        for(PatchVoiceRoom.UpdateRoom updateRoom : patchVoiceRoom.getUpdateRoomList()) {
            validateVoiceRoomInSpace(spaceId, updateRoom.getRoomId());
        }

        voiceRoomService.updateVoiceRoom(patchVoiceRoom.getUpdateRoomList());

        return new BaseResponse<>("success");
    }

    @DeleteMapping("/{voiceRoomId}")
    public BaseResponse<String> deleteVoiceRoom(
            @PathVariable("spaceId") @NotNull long spaceId,
            @JwtLoginAuth Long userId,
            @PathVariable("voiceRoomId") @NotNull Long voiceRoomId
    ){
        //해당 유저가 voice이 있는 space에 포함되어 있는지(권한이 있는지) 확인
        validateIsUserInSpace(spaceId,userId);
        //해당 유저가 현재 space에 대해 관리자 권한을 갖고 있는지 확인
        validateManagerPermission(spaceId,userId);
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

    private void validateIsUserInSpace( Long spaceId,Long userId) {
        // 유저가 스페이스에 속할 경우 exception이 터지지 않을 것임
        // 그렇지 않을 경우, USER_IS_NOT_IN_SPACE 예외가 터질 것임 -> 추후 exception handling 과정 필요

        //현재는 스페이스 접근 권한을 일괄적으로 예외 처리
        //분리 가능성 및 효용성 검토 필요
        userSpaceUtils.isUserInSpace(userId, spaceId);
    }
    private boolean validateVoiceRoom(long voiceRoomId){
        //Todo 해당 보이스룸이 존재하는지 확인
        if(!voiceRoomRepository.existsByVoiceRoomId(voiceRoomId)){
            throw new VoiceRoomException(VOICEROOM_NOT_EXIST);
        }
        return true;
    }
    private boolean validateVoiceRoomNameExist(String voiceRoomName){
        if(!voiceRoomRepository.existsByName(voiceRoomName)){
            throw new VoiceRoomException(VOICEROOM_NAME_ALREADY_EXIST);
        }
        return true;
    }
    private boolean validateVoiceRoomInSpace(long spaceId,long voiceRoomId){
        if(! (voiceRoomRepository.findById(voiceRoomId).getSpace().getSpaceId().equals(spaceId))){
            throw new VoiceRoomException(VOICEROOM_NOT_IN_SPACE);
        }
        return true;
    }
    private boolean validateManagerPermission(long spaceId,long userId){
        //해당 유저가 현재 space에 대해 관리자 권한을 갖고 있는지 확인
            //TODO 권한 확인 과정을 일괄적으로 처리 할 수 있는 코드가 필요해 보임
        User user =  userUtils.findUserByUserId(userId);
        Space space = spaceUtils.findSpaceBySpaceId(spaceId);
        //이미 userSpace 존재 여부를 검사해서 null 검사는 생략함

        if(!userSpaceDao.findUserSpaceByUserAndSpace(user,space).get().getUserSpaceAuth().toString().equals(MANAGER.getAuth())){
            System.out.print("Author :" +userSpaceDao.findUserSpaceByUserAndSpace(user,space).get().getUserSpaceAuth().toString());
            throw new VoiceRoomException(VOICEROOM_DO_NOT_HAVE_PERMISSION);
        }
        return true;
    }
}
