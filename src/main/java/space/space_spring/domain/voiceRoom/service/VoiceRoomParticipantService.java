package space.space_spring.domain.voiceRoom.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import space.space_spring.domain.voiceRoom.repository.VoiceRoomRepository;
import space.space_spring.domain.space.model.entity.Space;
import space.space_spring.domain.user.repository.UserDao;
import space.space_spring.domain.userSpace.repository.UserSpaceDao;
import space.space_spring.domain.voiceRoom.model.dto.GetParticipantList;
import space.space_spring.domain.voiceRoom.model.dto.ParticipantDto;
import space.space_spring.domain.voiceRoom.model.dto.ParticipantListDto;
import space.space_spring.domain.voiceRoom.model.dto.RoomDto;
import space.space_spring.exception.CustomException;
import space.space_spring.util.LiveKitUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static space.space_spring.response.status.BaseExceptionResponseStatus.VOICEROOM_NOT_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
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
        Space space = voiceRoomRepository.findById(voiceRoomId).orElseThrow(()->new CustomException(VOICEROOM_NOT_EXIST)).getSpace();
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
    public Map<Long,ParticipantListDto> getParticipantList(List<Long> roomIdList){

        Map<Long,CompletableFuture<ParticipantListDto>> futureMap = roomIdList.stream()
                .collect(Collectors.toMap(
                        roomId->roomId,
                        roomId->CompletableFuture.supplyAsync(
                                ()-> getParticipantDtoListById(roomId),
                                taskExecutor

                        ).exceptionally(throwable -> {
                            log.error("failed to fetch and get participantList",throwable);
                            return null;//empty ParticipantListDto
                        })
                ));
        try {
            // 모든 Future의 완료를 기다림
            CompletableFuture.allOf(
                futureMap.values().toArray(new CompletableFuture[0]))
                .exceptionally(throwable -> {
                    log.error("Error while waiting for all participant fetches to complete",throwable);
                    return null;
                })
                .join();

            return futureMap.entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> entry.getKey(),
                            entry->entry.getValue().getNow(ParticipantListDto.empty())
                    ));

        }catch (Exception e){
            log.error("Critical error while processing participant fetches", e);
            return Collections.emptyMap();  // 심각한 오류 발생 시 빈 Map 반환
        }
    }
}
