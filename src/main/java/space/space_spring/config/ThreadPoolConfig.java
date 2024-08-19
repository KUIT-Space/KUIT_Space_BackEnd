package space.space_spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ThreadPoolConfig {

    @Bean
    @Primary
    public TaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        int JVMCore = Runtime.getRuntime().availableProcessors();
        executor.setCorePoolSize(JVMCore <= 50 ? JVMCore : 50); // 기본적으로 생성할 스레드 수
        executor.setMaxPoolSize(JVMCore); // 최대 스레드 수
        executor.setQueueCapacity(200); // 큐의 크기 (요청이 초과되면 큐에 대기)
        executor.setThreadNamePrefix("CustomExecutor-"); // 스레드 이름 접두사
        executor.setWaitForTasksToCompleteOnShutdown(true);//어플리케이션 종료시 작업 완료 대기
        executor.setAwaitTerminationSeconds(20);//최대 종료 대기 시간
        executor.initialize();
        return executor;
    }
}
