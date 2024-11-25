package space.space_spring.concurrency;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


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

    @Test
    void supplyAsyncb() throws ExecutionException, InterruptedException {

        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Thread task start");
            return "Thread: " + Thread.currentThread().getName();
        });

        //System.out.println(future.get());
        System.out.println("Thread: " + Thread.currentThread().getName());
    }



}
