package com.example.demo.batch.file2db.csv01;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import com.example.demo.common.entity.Users;
import com.example.demo.common.mapper.UsersMapper;
import com.example.demo.core.exception.BatchSkipPolicy;
import com.example.demo.core.listener.BatchChunkListener;
import com.example.demo.core.listener.JobListener;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UserImportConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final BatchChunkListener batchChunkListener;
    private final JobListener jobListener;

    private final UserImportProcessor userImportProcessor;
    private final UserImportWriter userImportWriter;
    private final UsersMapper usersMapper;
    
    @Bean
    public FlatFileItemReader<CsvUser> reader() {
        
        FlatFileItemReader<CsvUser> reader = new FlatFileItemReader<>();

        // ヘッダー行をスキップ
        reader.setLinesToSkip(1);
        reader.setResource(new FileSystemResource("input-data/user.csv"));
        reader.setLineMapper(new DefaultLineMapper<CsvUser>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames("id", "name", "department", "createdAt");
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<CsvUser>() {
                    {
                        setTargetType(CsvUser.class);
                    }
                });
            }
        });
        return reader;
    };

    @Bean
    public Tasklet trancateTasklet() {
        return (contribution, chunkContext) -> {
            usersMapper.truncate();
            return null;
        };
    }

    @Bean
    public Job fishShopJob() {
        return new JobBuilder("csv01job", jobRepository)
                .start(step1())
                .next(step2())
                .listener(jobListener)
                .build();
    }

    @Bean
    public Step step1() {
        return new StepBuilder("step1", jobRepository)
                .tasklet(trancateTasklet(), platformTransactionManager)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step step2() {
        return new StepBuilder("step2", jobRepository)
                .<CsvUser, Users>chunk(10, platformTransactionManager)
                .reader(reader())
                .processor(userImportProcessor)
                .writer(userImportWriter)
                .allowStartIfComplete(true)
                .faultTolerant()
                .skipPolicy(new BatchSkipPolicy())
                .listener(batchChunkListener)
                .build();
    }
}
