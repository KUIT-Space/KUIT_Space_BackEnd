package space.space_spring.domain.voiceRoom.service;

import livekit.LivekitModels;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.user.repository.UserDao;
import space.space_spring.domain.userSpace.repository.UserSpaceDao;
import space.space_spring.domain.voiceRoom.repository.VoiceRoomDao;
import space.space_spring.domain.voiceRoom.repository.VoiceRoomRepository;
import space.space_spring.domain.voiceRoom.model.dto.GetParticipantList;
import space.space_spring.domain.voiceRoom.model.dto.GetVoiceRoomList;
import space.space_spring.domain.voiceRoom.model.dto.ParticipantDto;
import space.space_spring.domain.voiceRoom.model.dto.ParticipantListDto;
import space.space_spring.domain.voiceRoom.model.dto.PatchVoiceRoom;
import space.space_spring.domain.voiceRoom.model.dto.PostVoiceRoomDto;
import space.space_spring.domain.voiceRoom.model.dto.RoomDto;
import space.space_spring.domain.voiceRoom.model.dto.VoiceRoomListDto;
import space.space_spring.domain.space.model.entity.Space;
import space.space_spring.domain.user.model.entity.User;
import space.space_spring.domain.voiceRoom.model.entity.VoiceRoom;
import space.space_spring.exception.CustomException;
import space.space_spring.util.LiveKitUtils;
import space.space_spring.util.space.SpaceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import java.util.Map;

import static space.space_spring.response.status.BaseExceptionResponseStatus.VOICEROOM_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class VoiceRoomService {
    @Autowired
    private final VoiceRoomRepository voiceRoomRepository;
    private final VoiceRoomDao voiceRoomDao;
    private final SpaceUtils spaceUtils;
    private final LiveKitService liveKitService;
    private final LiveKitUtils liveKitUtils;
    private final UserDao userDao;
    private final UserSpaceDao userSpaceDao;
    private final TaskExecutor taskExecutor;
    private final VoiceRoomParticipantService voiceRoomParticipantService;

    public Long createVoiceRoom(long spaceId, PostVoiceRoomDto.Request req){
        Space targetSpace = spaceUtils.findSpaceBySpaceId(spaceId);
        Integer orderInt = voiceRoomRepository.findMaxOrderBySpace(targetSpace);
        //Space currentSpace=voiceRoomRepository.findActiveVoiceRoomsBySpaceId(spaceId);
        int order;
        if(orderInt==null||orderInt==0){
            order=1;
        }else{
            order=orderInt.intValue()+1;
        }
        String name = req.getName();

        return voiceRoomDao.createVoiceRoom(name, order, targetSpace);
    }



    /**
     // room 정보 가져오기 병렬적용
     */
    public List<GetVoiceRoomList.VoiceRoomInfo> getVoiceRoomInfoListConcurrency(long spaceId, GetVoiceRoomList.Request req){
        Integer limit = req.getLimit();
        boolean showParticipant =req.isShowParticipant();


        //해당 space VoiceRoom 가져오기 (VoiceRoom List)
        //Todo 가져오기에 limit 적용
        //todo 일급 객체 `voiceRoomEntityList`로 변경
        //List<VoiceRoom> voiceRoomDataList = findBySpaceId(spaceId);
        List<VoiceRoom> voiceRoomDataList = voiceRoomRepository.findActiveVoiceRoomsBySpaceId(spaceId);
        //todo voiceRoomEntityList 객체에 책임 위임
        //todo RoomDtoList 일급 객체 생성
        List<RoomDto> roomDtoList = RoomDto.convertRoomDtoListByVoiceRoom(voiceRoomDataList);

        //VoiceRoom과 Room mapping
        //#1 Response 받아오기
        List<LivekitModels.Room> roomResponsesTemp = liveKitUtils.getRoomList();

        //불변 list로 변환
        List<LivekitModels.Room> roomResponses = Collections.unmodifiableList(roomResponsesTemp);

        /**
         * 병렬 처리 적용대상 1
         */
        //#2 Room과 mapping 시키기
        List<Long> roomIdList = new ArrayList<>();
        for(RoomDto roomDto : roomDtoList){
            roomDto.setActiveRoom(roomResponses);
            roomIdList.add(roomDto.getId());
        }


        VoiceRoomListDto voiceRoomListDto = VoiceRoomListDto.from(voiceRoomDataList);
        voiceRoomListDto.setActiveRoom(roomResponsesTemp);





        Map<Long, ParticipantListDto> roomIdParticipantMap=voiceRoomParticipantService.getParticipantList(roomIdList);

        if(showParticipant){
            voiceRoomListDto.setParticipantListDto(roomIdParticipantMap);
        }

        //ToDo Response로 convert
        //#1 Active/inActive 분리

        //#2 convert
        if(limit==null||limit<=0) {
            return voiceRoomListDto.convertVoicRoomInfoList();
            //return GetVoiceRoomList.VoiceRoomInfo.convertRoomDtoList(roomDtoList);
        }else{
            return voiceRoomListDto.convertVoicRoomInfoList(limit);
            //return GetVoiceRoomList.VoiceRoomInfo.convertRoomDtoList(roomDtoList,limit);
        }
        //return null;
    }

    //todo 해당 함수의 책임을 RoomDto에게 위임
    private void setRoomDto(RoomDto roomDto,List<LivekitModels.Room> roomResponses,GetVoiceRoomList.Request req){
        roomDto.setActiveRoom(roomResponses);

        if(!req.isShowParticipant()){
            return;
        }

        if(roomDto.getNumParticipants()==0){
            //showParticipant = ture 일때, 참가자가 없으면 빈문자열[] 출력
            System.out.print("\n[DEBUG]Participant Number : 0\n");
            roomDto.setParticipantDTOList(Collections.emptyList());
            return;
        }
        //participantDto List 가져오기
        List<ParticipantDto> participantDtoList = getParticipantDtoListById(roomDto.getId());

        //RoomDto에 값 집어넣기
        //showParticipant = ture 일때, 참가자가 없으면 빈문자열[] 출력
        if(participantDtoList==null||participantDtoList.isEmpty()){
            System.out.print("\n\n[DEBUG]participant response is empty or null"+participantDtoList.toString()+
                    "participant number is \n\n");
            roomDto.setParticipantDTOList(Collections.emptyList());
        }else {
            roomDto.setParticipantDTOList(participantDtoList);
        }

    }


    public boolean updateVoiceRoom(List<PatchVoiceRoom.UpdateRoom> updateRoomList){

        //Todo 입력된 order가 유효한지 확인 필요

        //Todo 병렬적으로 update하도록 수정
        for(PatchVoiceRoom.UpdateRoom updateRoom : updateRoomList){
            VoiceRoom voiceRoom = voiceRoomRepository.findById(updateRoom.getRoomId()).get();
            String newName =updateRoom.getName();
            Integer newOrder= updateRoom.getOrder();
            voiceRoom.update(newName,newOrder);
            voiceRoomRepository.save(voiceRoom);

        }
        
        return true;
    }

    public void deleteVoiceRoom(long voiceRoomId){
        //Todo Base Entity에 일괄적으로 soft Delete를 적용하는 방법을 다같이 정하는 것이 좋아보임
        VoiceRoom voiceRoom = voiceRoomRepository.findById(voiceRoomId).orElseThrow(()->new CustomException(VOICEROOM_NOT_EXIST));
        voiceRoom.updateInactive();
        voiceRoomRepository.save(voiceRoom);
    }

    private String findProfileImageByUserId(Long userSpaceId){
        return userSpaceDao.findProfileImageById(userSpaceId).orElse("");
    }
//    public List<VoiceRoom> findBySpaceId(long spaceId){
//        return findBySpace(spaceUtils.findSpaceBySpaceId(spaceId));
//    }
//    private List<VoiceRoom> findBySpace(Space space){
//        return voiceRoomRepository.findBySpace(space);
//    }
    private List<ParticipantDto> getParticipantDtoListById(long voiceRoomId){
        Space space = voiceRoomRepository.findById(voiceRoomId).orElseThrow(()->new CustomException(VOICEROOM_NOT_EXIST)).getSpace();
        List<ParticipantDto> participantDtoList =  liveKitUtils.getParticipantInfo(String.valueOf(voiceRoomId));
        if(participantDtoList==null||participantDtoList.isEmpty()){
            return Collections.emptyList();
        }
        for(ParticipantDto participantDto: participantDtoList){
            //profileIamge 집어넣기
            participantDto.setProfileImage(findProfileImageByUserId(participantDto.getUserSpaceId()));
            //userSpaceId 집어 넣기
            User user = userDao.findUserByUserId(participantDto.getId());
            participantDto.setUserSpaceId(userSpaceDao.findUserSpaceByUserAndSpace(user,space).get().getUserSpaceId());
        }
        return participantDtoList;
    }


    public List<GetParticipantList.ParticipantInfo> getParticipantInfoListById(long voiceRoomId){
        return GetParticipantList.ParticipantInfo.convertParticipantDtoList(
                getParticipantDtoListById(voiceRoomId)
                //liveKitUtils.getParticipantInfo(findNameTagById(voiceRoomId))
        );
    }

    public String getToken(long spaceId,long userId,long userSpaceId,long voiceRoomId){
        String userName=userSpaceDao.findUserNameById(userSpaceId);
        String userIdentity=String.valueOf(userSpaceId);
        //Metadata에 profileImage와 userName 추가
        String metadata="userProfileImage : "+userSpaceDao.findProfileImageById(userSpaceId).orElse("");
        //String roomName=findNameTagById(voiceRoomId);
        String roomName = String.valueOf(voiceRoomId);
        return liveKitUtils.getRoomToken(userName,userIdentity,roomName,metadata).toJwt();
    }
}
