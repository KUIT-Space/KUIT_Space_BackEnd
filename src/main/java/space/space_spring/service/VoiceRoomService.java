package space.space_spring.service;

import livekit.LivekitModels;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import space.space_spring.domain.user.repository.UserDao;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dao.VoiceRoomDao;
import space.space_spring.dao.VoiceRoomRepository;
import space.space_spring.dto.VoiceRoom.*;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.VoiceRoom;
import space.space_spring.util.LiveKitUtils;
import space.space_spring.util.space.SpaceUtils;

import java.util.Collections;
import java.util.List;

import java.util.Map;
import java.util.Optional;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public Long createVoiceRoom(long spaceId,PostVoiceRoomDto.Request req){
        Space targetSpace = spaceUtils.findSpaceBySpaceId(spaceId);
        Integer orderInt = voiceRoomRepository.findMaxOrderBySpace(targetSpace);
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
     // room 정보 가져오기 병렬적용 Ver - 2
     */
    public List<GetVoiceRoomList.VoiceRoomInfo> getVoiceRoomInfoListConcurrency(long spaceId,GetVoiceRoomList.Request req){
        Integer limit = req.getLimit();
        boolean showParticipant =req.isShowParticipant();


        //해당 space VoiceRoom 가져오기 (VoiceRoom List)
        //Todo 가져오기에 limit 적용
        //todo 일급 객체 `voiceRoomEntityList`로 변경
        List<VoiceRoom> voiceRoomDataList = findBySpaceId(spaceId);
        //todo voiceRoomEntityList 객체에 책임 위임
        //todo RoomDtoList 일급 객체 생성
        List<RoomDto> roomDtoList = RoomDto.convertRoomDtoListByVoiceRoom(voiceRoomDataList);

        //VoiceRoom과 Room mapping
        //#1 Response 받아오기
        List<LivekitModels.Room> roomResponsesTemp = liveKitUtils.getRoomList();

        //불변 list로 변환
        List<LivekitModels.Room> roomResponses = Collections.unmodifiableList(roomResponsesTemp);

        VoiceRoomDtoList voiceRoomDtoList=VoiceRoomDtoList.from(voiceRoomDataList);
        voiceRoomDtoList.setActiveRoom(roomResponsesTemp);


        //ToDo setRoomDto 함수를 RoomDtoList 객체로 이동
            //todo 책임을 위임해도 이 병렬처리 코드가 잘 동작할까?
        List<CompletableFuture<Void>> roomDtoFutureList = roomDtoList.stream()
                .map(r->CompletableFuture.runAsync(()->setRoomDto(r,roomResponses,req),taskExecutor)
                        //.exceptionally(ex->{throws ex;})
                )
                .collect(Collectors.toList());


        // 모든 Future의 완료를 기다림
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                roomDtoFutureList.toArray(new CompletableFuture[0]));

        // 결과 수집 및 출력
        allOf.join();
        //ToDo Response로 convert
        //#1 Active/inActive 분리

        //#2 convert
        if(limit==null||limit<=0) {
            return voiceRoomDtoList.convertVoicRoomInfoList();
            //return GetVoiceRoomList.VoiceRoomInfo.convertRoomDtoList(roomDtoList);
        }else{
            return voiceRoomDtoList.convertVoicRoomInfoList(limit);
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
        VoiceRoom voiceRoom = voiceRoomRepository.findById(voiceRoomId);
        voiceRoom.updateInactive();
        voiceRoomRepository.save(voiceRoom);
    }

    private String findProfileImageByUserId(Long userSpaceId){
        return userSpaceDao.findProfileImageById(userSpaceId).orElse("");
    }
    public List<VoiceRoom> findBySpaceId(long spaceId){
        return findBySpace(spaceUtils.findSpaceBySpaceId(spaceId));
    }
    private List<VoiceRoom> findBySpace(Space space){
        return voiceRoomRepository.findBySpace(space);
    }
    private List<ParticipantDto> getParticipantDtoListById(long voiceRoomId){
        Space space = voiceRoomRepository.findById(voiceRoomId).getSpace();
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
        String userIdentity=String.valueOf(userId);
        //Metadata에 profileImage와 userName 추가
        String metadata="userProfileImage : "+userSpaceDao.findProfileImageById(userSpaceId).orElse("");
        //String roomName=findNameTagById(voiceRoomId);
        String roomName = String.valueOf(voiceRoomId);
        return liveKitUtils.getRoomToken(userName,userIdentity,roomName,metadata).toJwt();
    }
}
