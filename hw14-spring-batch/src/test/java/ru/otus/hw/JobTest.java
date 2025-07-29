package ru.otus.hw;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
public class JobTest {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    @Qualifier("migrateDataToMongoJob")
    private Job migrateDataToMongoJob;

    @Autowired
    @Qualifier("cleanMongoJob")
    private Job cleanMongoJob;

    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void testMigrateJob() throws Exception {
        JobExecution jobExecution = jobLauncher.run(migrateDataToMongoJob, new JobParametersBuilder().toJobParameters());
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo(BatchStatus.COMPLETED.name());
    }


    @Test
    void testCleanJob() throws Exception {
        JobExecution jobExecution = jobLauncher.run(cleanMongoJob, new JobParametersBuilder().toJobParameters());
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo(BatchStatus.COMPLETED.name());
    }
}
