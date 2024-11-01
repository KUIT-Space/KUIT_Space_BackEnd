package space.space_spring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;
import space.space_spring.dao.UserSpaceDao;
import space.space_spring.dao.VoiceRoomRepository;
import space.space_spring.domain.user.repository.UserDao;
import space.space_spring.dto.VoiceRoom.GetParticipantList;
import space.space_spring.dto.VoiceRoom.ParticipantDto;
import space.space_spring.dto.VoiceRoom.ParticipantListDto;
import space.space_spring.dto.VoiceRoom.RoomDto;
import space.space_spring.entity.Space;
import space.space_spring.entity.User;
import space.space_spring.entity.UserSpace;
import space.space_spring.util.LiveKitUtils;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoiceRoomParticipantService {
    final private UserSpaceDao userSpaceDao;
    final private UserDao userDao;
    final private VoiceRoomRepository voiceRoomRepository;
    final private LiveKitUtils liveKitUtils;
    private final TaskExecutor taskExecutor;
    public List<GetParticipantList.ParticipantInfo> getParticipantInfoListById(long voiceRoomId){
        return getParticipantDtoListById(voiceRoomId).convertParticipantDtoList();
    }
    private ParticipantListDto getParticipantDtoListById(long voiceRoomId){
        Space space = voiceRoomRepository.findById(voiceRoomId).getSpace();
        //Todo 다른 네이밍 고려
        List<ParticipantDto> participantDtos =  liveKitUtils.getParticipantInfo(String.valueOf(voiceRoomId));
        if(participantDtos==null || participantDtos.isEmpty()){
            participantDtos =  Collections.emptyList();
        }

        ParticipantListDto participantListDto = ParticipantListDto.from(participantDtos);
        participantListDto.setProfileImage(this::findProfileImageByUserSpaceId);
        return participantListDto;
    }
    private String findProfileImageByUserSpaceId(Long userSpaceId){
        return userSpaceDao.findProfileImageById(userSpaceId).orElse("");
    }




    //1. 이 함수를 VoiceRoomListDto의 parameter로 넘긴다
    //장점. 동기 처리의 책임을 service가 질 수 있음
    //단점. 굳이 이렇게까지 코드를 꼬야야하나 싶을 수 있음. 그냥 getRoomList해서 RoomList를 밖으로 빼는게 나을지도

    //2. 이 함수를 VoiceRoomListDto 내부로 이전
    //장점. findProfileImageByUserSpaceId만 function parameter로 넘기면 됨 -> 책임과 구조가 더 명확하게 보인다고
    //단점. 비동기 병렬처리의 책임을 VoiceRoomListDto가 가져야함.
    public void setParticipant(List<RoomDto> roomDtoList){
        List<CompletableFuture<Void>> roomDtoFutureList = roomDtoList.stream()
                .map(r->CompletableFuture.runAsync(()->r.setParticipantDTOList(getParticipantDtoListById(r.getId())),taskExecutor)
                        //.exceptionally(ex->{throws ex;})
                )
                .collect(Collectors.toList());


        // 모든 Future의 완료를 기다림
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                roomDtoFutureList.toArray(new CompletableFuture[0]));

        // 결과 수집 및 출력
        allOf.join();
    }
}
