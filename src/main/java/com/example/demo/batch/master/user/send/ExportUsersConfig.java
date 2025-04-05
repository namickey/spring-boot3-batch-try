package com.example.demo.batch.master.user.send;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.common.entity.Users;
import com.example.demo.common.mapper.UsersMapper;
import com.example.demo.core.exception.BatchSkipPolicy;
import com.example.demo.core.listener.BatchChunkListener;
import com.example.demo.core.listener.JobListener;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ExportUsersConfig {

    private final JobRepository jobRepository;
    private final JobListener jobListener;
    private final SqlSessionFactory sqlSessionFactory;
    private final PlatformTransactionManager platformTransactionManager;
    private final BatchChunkListener batchChunkListener;

    private final ExportUsersProcessor userExportProcessor;
    private final ExportUsersFieldExtractor userExportFieldExtractor;

    @Bean
    public ItemReader<? extends Users> userExportReader() {
        MyBatisCursorItemReaderBuilder<Users> reader = new MyBatisCursorItemReaderBuilder<Users>();
        return reader
                .sqlSessionFactory(sqlSessionFactory)
                .queryId(UsersMapper.class.getName() + ".selectAll")
                .build();
    }

    @Bean
    public FlatFileItemWriter<ExportUsersItem> userExportWriter() {
        return new FlatFileItemWriterBuilder<ExportUsersItem>()
                .encoding("UTF-8")
                .name("userExportWriter")
                .saveState(false)
                .resource(new PathResource("output-data/users.csv"))
                .lineSeparator("\r\n")
                .shouldDeleteIfEmpty(false)
                .delimited()
                .delimiter(",")
                .fieldExtractor(userExportFieldExtractor)
                .build();
    }

    @Bean
    public Job exportUsersJob() {
        return new JobBuilder("exportUsersJob", jobRepository)
                .start(exportUsersStep1())
                .listener(jobListener)
                .build();
    }

    @Bean
    public Step exportUsersStep1() {
        return new StepBuilder("exportUsersStep1", jobRepository)
                .<Users, ExportUsersItem>chunk(10, platformTransactionManager)
                .reader(userExportReader())
                .processor(userExportProcessor)
                .writer(userExportWriter())
                .allowStartIfComplete(true)
                .faultTolerant()
                .skipPolicy(new BatchSkipPolicy())
                .listener(batchChunkListener)
                .build();
    }
}
