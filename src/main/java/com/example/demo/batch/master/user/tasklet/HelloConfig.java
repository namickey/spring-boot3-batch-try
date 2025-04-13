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

/** コンフィグ */
@Configuration
@RequiredArgsConstructor
public class HelloConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    /** タスクレット */
    @Bean
    public Tasklet helloTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("************ hello world! ************");
            return null;
        };
    }

    /** ジョブ */
    @Bean
    public Job helloJob() {
        return new JobBuilder("helloJob", jobRepository)
                .start(helloStep())
                .build();
    }

    /** ステップ */
    @Bean
    public Step helloStep() {
        return new StepBuilder("helloStep", jobRepository)
                .tasklet(helloTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)// true:実行履歴があっても何度でも再実行可能。false:一度だけ実行可能
                .build();
    }
}
