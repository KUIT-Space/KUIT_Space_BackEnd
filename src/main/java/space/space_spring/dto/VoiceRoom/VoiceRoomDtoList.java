package space.space_spring.dto.VoiceRoom;

import livekit.LivekitModels;
import lombok.RequiredArgsConstructor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import space.space_spring.controller.VoiceRoomController;
import space.space_spring.dao.UserDao;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dao.VoiceRoomRepository;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.VoiceRoom;
import space.space_spring.util.LiveKitUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class VoiceRoomDtoList {
    private List<RoomDto> roomDtoList;
//    private final LiveKitUtils liveKitUtils;
//    private final VoiceRoomRepository voiceRoomRepository;
//    private final UserSpaceDao userSpaceDao;
//    private final UserDao userDao;
//    private final TaskExecutor taskExecutor;
    public  VoiceRoomDtoList(List<RoomDto> roomDtos){
        this.roomDtoList=roomDtos;
    }

    public static VoiceRoomDtoList from(List<VoiceRoom> voiceRoomEntityList){
        List<RoomDto> roomDtos = convertRoomDtoListByVoiceRoom(voiceRoomEntityList);
        return new VoiceRoomDtoList(roomDtos);
    }

    public static List<RoomDto> convertRoomDtoListByVoiceRoom(List<VoiceRoom> voiceRoomList){
        if(voiceRoomList==null||voiceRoomList.isEmpty()){return null;}
        return voiceRoomList.stream()
                .map(RoomDto::convertRoom)
                .collect(Collectors.toList());
    }

    private void setActiveRoom(Map<String,LivekitModels.Room> roomResponse){
        for(RoomDto room : this.roomDtoList){
            LivekitModels.Room resRoom= roomResponse.get(String.valueOf(room.getId()));
            if(resRoom==null){continue;}
            room.setActiveRoom(resRoom);
        }

    }
    public void setActiveRoom(List<LivekitModels.Room> roomResponse){
        Map<String,LivekitModels.Room> roomResMap=roomResponse.stream()
                .collect(Collectors.toMap(
                        res->res.getName(),
                        res->res,
                        (oldVal,newVal)->newVal
                ));
        roomResMap = Collections.unmodifiableMap(roomResMap);
        setActiveRoom(roomResMap);
    }

    public List<GetVoiceRoomList.VoiceRoomInfo> convertVoicRoomInfoList(Integer limit){
        if(this.roomDtoList==null||this.roomDtoList.isEmpty()){return null;}
        Stream<RoomDto> sortedStream = this.roomDtoList.stream()
                .sorted(Comparator
                        .comparing((RoomDto r) -> r.getNumParticipants() == 0) // Active rooms first
                        .thenComparing(RoomDto::getOrder)) ;// Then by order
        System.out.print("limit input:"+limit);
        Stream<RoomDto> processedStream = (limit != null) ? sortedStream.limit(limit) : sortedStream;

        return processedStream.map(GetVoiceRoomList.VoiceRoomInfo::convertRoomDto)
                .collect(Collectors.toList());
    }
    public List<GetVoiceRoomList.VoiceRoomInfo> convertVoicRoomInfoList() {
        return convertVoicRoomInfoList( null);
    }

//    public void setActiveRoom(List<LivekitModels.Room> roomResponsesTemp){
//        //#1 Response 받아오기
//
//        //불변 list로 변환
//        List<LivekitModels.Room> roomResponses = Collections.unmodifiableList(roomResponsesTemp);
//        /**
//         * 병렬 처리 적용대상 1
//         */
//        //#2 Room과 mapping 시키기
//        for(RoomDto roomDto : roomDtoList){
//            roomDto.setActiveRoom(roomResponses);
//        }
//        //ToDo setRoomDto 함수를 RoomDtoList 객체로 이동
//        //todo 책임을 위임해도 이 병렬처리 코드가 잘 동작할까?
//        List<CompletableFuture<Void>> roomDtoFutureList = roomDtoList.stream()
//                .map(r->CompletableFuture.runAsync(()->setRoomDto(r,roomResponses,req),taskExecutor)
//                        //.exceptionally(ex->{throws ex;})
//                )
//                .collect(Collectors.toList());
//
//
//        // 모든 Future의 완료를 기다림
//        CompletableFuture<Void> allOf = CompletableFuture.allOf(
//                roomDtoFutureList.toArray(new CompletableFuture[0]));
//
//        // 결과 수집 및 출력
//        allOf.join();
//        //ToDo Response로 convert
//        //#1 Active/inActive 분리
//    }

//    public void setRoomDto (RoomDto roomDto,List<LivekitModels.Room> roomResponses,GetVoiceRoomList.Request req){
//        roomDto.setActiveRoom(roomResponses);
//
//        if(!req.isShowParticipant()){
//            return;
//        }
//
//        if(roomDto.getNumParticipants()==0){
//            //showParticipant = ture 일때, 참가자가 없으면 빈문자열[] 출력
//            System.out.print("\n[DEBUG]Participant Number : 0\n");
//            roomDto.setParticipantDTOList(Collections.emptyList());
//            return;
//        }
//        //participantDto List 가져오기
//        List<ParticipantDto> participantDtoList = getParticipantDtoListById(roomDto.getId());
//
//        //RoomDto에 값 집어넣기
//        //showParticipant = ture 일때, 참가자가 없으면 빈문자열[] 출력
//        if(participantDtoList==null||participantDtoList.isEmpty()){
//            System.out.print("\n\n[DEBUG]participant response is empty or null"+participantDtoList.toString()+
//                    "participant number is \n\n");
//            roomDto.setParticipantDTOList(Collections.emptyList());
//        }else {
//            roomDto.setParticipantDTOList(participantDtoList);
//        }
//
//    }
//
//    private List<ParticipantDto> getParticipantDtoListById(long voiceRoomId){
//        Space space = voiceRoomRepository.findById(voiceRoomId).getSpace();
//        List<ParticipantDto> participantDtoList =  liveKitUtils.getParticipantInfo(String.valueOf(voiceRoomId));
//        if(participantDtoList==null||participantDtoList.isEmpty()){
//            return Collections.emptyList();
//        }
//        for(ParticipantDto participantDto: participantDtoList){
//            //profileIamge 집어넣기
//            participantDto.setProfileImage(findProfileImageByUserId(participantDto.getUserSpaceId()));
//            //userSpaceId 집어 넣기
//            User user = userDao.findUserByUserId(participantDto.getId());
//            participantDto.setUserSpaceId(userSpaceDao.findUserSpaceByUserAndSpace(user,space).get().getUserSpaceId());
//        }
//        return participantDtoList;
//    }
//    private String findProfileImageByUserId(Long userSpaceId){
//        return userSpaceDao.findProfileImageById(userSpaceId).orElse("");
//    }


}
