package space.space_spring.concurrency;

import livekit.LivekitModels;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import space.space_spring.config.ThreadPoolConfig;
import space.space_spring.dao.VoiceRoomRepository;
import space.space_spring.dto.VoiceRoom.RoomDto;
import space.space_spring.entity.Space;
import space.space_spring.entity.VoiceRoom;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import space.space_spring.util.LiveKitUtils;
import space.space_spring.util.space.SpaceUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.mockito.Mockito.when;

//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

    @Autowired
    private TaskExecutor taskExecutor;

    @Test
    public void testThreadPoolExecution() throws InterruptedException {

        ThreadPoolConfig config = new ThreadPoolConfig();
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) config.threadPoolTaskExecutor();


        int taskCount = 100;
        CountDownLatch latch = new CountDownLatch(taskCount);
        Set<String> threadNames = Collections.synchronizedSet(new HashSet<>());

        for (int i = 0; i < taskCount; i++) {
            executor.execute(() -> {
                String threadName = Thread.currentThread().getName();
                threadNames.add(threadName);
                try {
                    Thread.sleep(100);  // 작업 시뮬레이션
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                latch.countDown();
            });
        }

        latch.await(10, TimeUnit.SECONDS);  // 최대 10초 대기

        System.out.println("총 사용된 쓰레드 수: " + threadNames.size());
        System.out.println("사용된 쓰레드 이름들: " + threadNames);

        assertTrue(threadNames.size() > 1, "여러 쓰레드가 사용되어야 합니다.");
        assertTrue(threadNames.stream().allMatch(name -> name.startsWith("CustomExecutor-")),
                "모든 쓰레드 이름은 'CustomExecutor-'로 시작해야 합니다.");
    }



}
