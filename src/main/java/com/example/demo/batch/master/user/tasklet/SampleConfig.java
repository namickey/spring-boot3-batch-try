package com.example.demo.batch.master.user.tasklet;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SampleConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Tasklet sampleTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("************ hello world! ************");
            return null;
        };
    }

    /**
     * ジョブ
     */
    @Bean
    public Job sampleTaskletJob() {
        return new JobBuilder("sampleTaskletJob", jobRepository)
                .start(sampleTaskletStep1())
                .build();
    }

    /**
     * ステップ１
     */
    @Bean
    public Step sampleTaskletStep1() {
        return new StepBuilder("sampleTaskletStep1", jobRepository)
                .tasklet(sampleTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }
}
