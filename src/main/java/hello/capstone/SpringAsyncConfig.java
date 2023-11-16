package hello.capstone;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync // Application이 아닌, Async 설정 클래스에 붙여야 함.
public class SpringAsyncConfig {

    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(5); 
        taskExecutor.setMaxPoolSize(50); 
        taskExecutor.setQueueCapacity(100); 
        taskExecutor.setThreadNamePrefix("Executor-");
        return taskExecutor;
    }

}