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
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.projections.AuthorMongoProjection;
import ru.otus.hw.projections.BookMongoProjection;
import org.springframework.data.mongodb.core.query.Query;
import ru.otus.hw.projections.CommentMongoProjection;
import ru.otus.hw.projections.GenreMongoProjection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ImportAutoConfiguration(
        exclude = {
                org.springframework.shell.boot.StandardCommandsAutoConfiguration.class
        }
)
@SpringBatchTest
public class JobTest {

    @Autowired
    private MongoTemplate mongoTemplate;

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
        mongoTemplate.getDb().drop();
    }

    @Test
    void testMigrateJob() throws Exception {
        JobExecution jobExecution = jobLauncher.run(migrateDataToMongoJob, new JobParametersBuilder().toJobParameters());
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo(BatchStatus.COMPLETED.name());
        assertEquals(3, mongoTemplate.count(new Query(), BookMongoProjection.class));
        assertEquals(3, mongoTemplate.count(new Query(), CommentMongoProjection.class));
        assertEquals(3, mongoTemplate.count(new Query(), GenreMongoProjection.class));
        assertEquals(3, mongoTemplate.count(new Query(), AuthorMongoProjection.class));
    }

    @Test
    void testCleanJob() throws Exception {
        AuthorMongoProjection authorMongoProjection = new AuthorMongoProjection("1", "a");
        GenreMongoProjection genreMongoProjection = new GenreMongoProjection("2", "b");
        BookMongoProjection bookMongoProjection = new BookMongoProjection("3", "c", authorMongoProjection, genreMongoProjection);
        CommentMongoProjection commentMongoProjection = new CommentMongoProjection("4", "d", bookMongoProjection);
        mongoTemplate.save(authorMongoProjection);
        mongoTemplate.save(genreMongoProjection);
        mongoTemplate.save(bookMongoProjection);
        mongoTemplate.save(commentMongoProjection);
        JobExecution jobExecution = jobLauncher.run(cleanMongoJob, new JobParametersBuilder().toJobParameters());
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo(BatchStatus.COMPLETED.name());
        assertEquals(0, mongoTemplate.count(new Query(), BookMongoProjection.class));
        assertEquals(0, mongoTemplate.count(new Query(), CommentMongoProjection.class));
        assertEquals(0, mongoTemplate.count(new Query(), GenreMongoProjection.class));
        assertEquals(0, mongoTemplate.count(new Query(), AuthorMongoProjection.class));
    }
}
