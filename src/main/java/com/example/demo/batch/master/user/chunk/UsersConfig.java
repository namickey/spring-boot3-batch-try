package com.example.demo.batch.master.user.chunk;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.common.entity.Users;
import com.example.demo.common.mapper.UsersMapper;

import lombok.RequiredArgsConstructor;

/** コンフィグ */
@Configuration
@RequiredArgsConstructor
public class UsersConfig {
    
    private final JobRepository jobRepository;
    private final SqlSessionFactory sqlSessionFactory;
    private final PlatformTransactionManager platformTransactionManager;

    private final UsersProcessor usersProcessor;
    private final UsersWriter usersWriter;

    /** データリーダー */
    @Bean
    public ItemReader<? extends Users> userReader() {
        MyBatisCursorItemReaderBuilder<Users> reader = new MyBatisCursorItemReaderBuilder<Users>();
        return reader
                .sqlSessionFactory(sqlSessionFactory)
                .queryId(UsersMapper.class.getName() + ".selectAll")
                .build();
    }

    /** ジョブ */
    @Bean
    public Job usersJob() {
        return new JobBuilder("usersJob", jobRepository)
                .start(usersStep1())
                .build();
    }

    /** ステップ */
    @Bean
    public Step usersStep1() {
        return new StepBuilder("usersStep1", jobRepository)
                .<Users, Users>chunk(10, platformTransactionManager)
                .reader(userReader())
                .processor(usersProcessor)
                .writer(usersWriter)
                .allowStartIfComplete(true) // true:実行履歴があっても何度でも再実行可能。false:一度だけ実行可能
                .build();
    }
}
