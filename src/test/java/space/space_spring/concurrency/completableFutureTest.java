package space.space_spring.concurrency;

import livekit.LivekitModels;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import space.space_spring.dao.VoiceRoomRepository;
import space.space_spring.dto.VoiceRoom.RoomDto;
import space.space_spring.entity.Space;
import space.space_spring.entity.VoiceRoom;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import space.space_spring.util.LiveKitUtils;
import space.space_spring.util.space.SpaceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;


public class completableFutureTest {
    @Test
    void runAsync() throws ExecutionException, InterruptedException {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            try{Thread.sleep(1000L);}catch (InterruptedException e){}
            System.out.println("Thread: " + Thread.currentThread().getName());
        });
        System.out.println("Before call Get ");
        future.get();
        System.out.println("Thread: " + Thread.currentThread().getName());
    }
    // 출처: https://mangkyu.tistory.com/263 [MangKyu's Diary:티스토리]

    @Test
    void supplyAsync() throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            try{Thread.sleep(1000L);}catch (InterruptedException e){}
            return "Thread: " + Thread.currentThread().getName();
        });

        System.out.println(future.get());
        System.out.println("Thread: " + Thread.currentThread().getName());
    }
    //출처: https://mangkyu.tistory.com/263 [MangKyu's Diary:티스토리]

    @Test
    public void ThreadAfterThread(){
        List<String> strings = List.of("Hi", "CompletableFuture", "OK", "Processing");

        List<CompletableFuture<Integer>> futures = processStringsAsync(strings);

        // 모든 Future의 완료를 기다림
        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));

        // 결과 수집 및 출력
        allOf.thenRun(() -> {
            List<Integer> results = futures.stream()
                    .map(CompletableFuture::join)
                    .collect(Collectors.toList());
            System.out.println("Processed string lengths: " + results);
        }).join();

        // 개별 결과 출력 (순서 무관)
        for (int i = 0; i < strings.size(); i++) {
            final int index = i;
            futures.get(i).thenAccept(result ->
                    System.out.println("Result for '" + strings.get(index) + "': " + result)
            );
        }
    }
    public static List<CompletableFuture<Integer>> processStringsAsync(List<String> strings) {
        return strings.stream()
                .map(s -> CompletableFuture.supplyAsync(() -> s.length())
                        .thenCompose(length -> CompletableFuture.supplyAsync(()->length <= 3 ? length * 10 : length)))
                .collect(Collectors.toList());
    }
    @Mock
    private VoiceRoomRepository repository;
    @Mock
    private SpaceUtils spaceUtils;
    @Mock
    private LiveKitUtils liveKitUtils;
    @ParameterizedTest
    @ValueSource(booleans =  {true, false})
    public void getRoomListTest(boolean showParticipant){
        long spaceId = 1;
        Space space1 = new Space();
        space1.saveSpace("space1","asdf");
        VoiceRoom voiceRoom1= VoiceRoom.createVoiceRoom("room1",1,space1);
        VoiceRoom voiceRoom2= VoiceRoom.createVoiceRoom("room2",2,space1);
        VoiceRoom voiceRoom3= VoiceRoom.createVoiceRoom("room3",3,space1);
        List<VoiceRoom> voiceRoomList= List.of(voiceRoom1,voiceRoom2,voiceRoom3);
        List<LivekitModels.Room> roomList = new ArrayList<LivekitModels.Room>();
        //roomList.add(LivekitModels.Room.newBuilder())
        //private static List<VoiceRoom> findBySpace(long userID){return null;}
        when(spaceUtils.findSpaceBySpaceId(spaceId)).thenReturn(space1);
        when(repository.findBySpace(spaceUtils.findSpaceBySpaceId(spaceId))).thenReturn(voiceRoomList);
        //when(liveKitUtils.getRoomList()).thenReturn()

        List<VoiceRoom> voiceRoomDataList = repository.findBySpace(spaceUtils.findSpaceBySpaceId(spaceId));
        List<RoomDto> roomDtoList = RoomDto.convertRoomDtoListByVoiceRoom(voiceRoomDataList);



    }



}
