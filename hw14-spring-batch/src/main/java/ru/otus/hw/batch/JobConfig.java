package ru.otus.hw.batch;

import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.mappers.AuthorMapper;
import ru.otus.hw.mappers.BookMapper;
import ru.otus.hw.mappers.CommentMapper;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.Genre;
import ru.otus.hw.projections.AuthorMongoProjection;
import ru.otus.hw.projections.BookMongoProjection;
import ru.otus.hw.projections.CommentMongoProjection;
import ru.otus.hw.projections.GenreMongoProjection;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

@SuppressWarnings("unused")
@Configuration
@RequiredArgsConstructor
public class JobConfig {

    public static final  String MONGO_MIGRATION_JOB = "Migration";

    public static final String MONGO_CLEAN_JOB = "Clean";

    private static final int CHUNK_SIZE = 1;

    private static final int COMMON_PAGE_SIZE = 10;

    private final Logger logger = LoggerFactory.getLogger("Batch");

    private final JobRepository jobRepository;

    private final PlatformTransactionManager platformTransactionManager;

    private final EntityManagerFactory entityManagerFactory;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final CommentRepository commentRepository;

    private final GenreMapper genreMapper;

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    private final CommentMapper commentMapper;

    private final ThreadPoolTaskExecutor taskExecutor;

    @Bean
    public Job migrateDataToMongoJob(Step migrateAuthorStep, Step migrateGenreStep,
                                     Step migrateBookStep, Step migrateCommentStep) {
        Flow flowAuthorsAndGenres = new FlowBuilder<Flow>("authorsAndGenresFlow")
                .start(new FlowBuilder<Flow>("authorFlow").from(migrateAuthorStep).build())
                .split(taskExecutor)
                .add(new FlowBuilder<Flow>("genreFlow").from(migrateGenreStep).build())
                .build();

        return new JobBuilder(MONGO_MIGRATION_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(flowAuthorsAndGenres)
                .next(migrateBookStep)
                .next(migrateCommentStep)
                .end()
                .build();
    }

    @Bean
    public Job cleanMongoJob(Step cleanMongoStep) {
        return new JobBuilder(MONGO_CLEAN_JOB, jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(cleanMongoStep)
                .end()
                .build();
    }

    @Bean
    public Step migrateAuthorStep(AuthorMapper authorMapper) {
        return new StepBuilder("migrateAuthorStep", jobRepository)
                .<Author, AuthorMongoProjection>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(new JpaPagingItemReaderBuilder<Author>()
                        .name("authorReader")
                        .entityManagerFactory(entityManagerFactory)
                        .queryString("SELECT a FROM Author a")
                        .pageSize(COMMON_PAGE_SIZE)
                        .build())
                .processor(authorMapper::toMongoProjection)
                .writer(authorRepository::saveAll)
                .build();
    }

    @Bean
    public Step migrateGenreStep(GenreMapper genreMapper) {
        return new StepBuilder("migrateGenreStep", jobRepository)
                .<Genre, GenreMongoProjection>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(new JpaPagingItemReaderBuilder<Genre>()
                        .name("genreReader")
                        .entityManagerFactory(entityManagerFactory)
                        .queryString("SELECT g FROM Genre g")
                        .pageSize(COMMON_PAGE_SIZE)
                        .build())
                .processor(genreMapper::toMongoProjection)
                .writer(genreRepository::saveAll)
                .build();
    }

    @Bean
    public Step migrateBookStep(BookMapper bookMapper) {
        return new StepBuilder("migrateBookStep", jobRepository)
                .<Book, BookMongoProjection>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(new JpaPagingItemReaderBuilder<Book>()
                        .name("bookReader")
                        .entityManagerFactory(entityManagerFactory)
                        .queryString("SELECT b FROM Book b")
                        .pageSize(COMMON_PAGE_SIZE)
                        .build())
                .processor(bookMapper::toMongoProjection)
                .writer(bookRepository::saveAll)
                .build();
    }

    @Bean
    public Step migrateCommentStep(CommentMapper commentMapper) {
        return new StepBuilder("migrateCommentStep", jobRepository)
                .<Comment, CommentMongoProjection>chunk(CHUNK_SIZE, platformTransactionManager)
                .reader(new JpaPagingItemReaderBuilder<Comment>()
                        .name("commentReader")
                        .entityManagerFactory(entityManagerFactory)
                        .queryString("SELECT c FROM Comment c")
                        .pageSize(COMMON_PAGE_SIZE)
                        .build())
                .processor(commentMapper::toMongoProjection)
                .writer(commentRepository::saveAll)
                .build();
    }

    @Bean
    public Step cleanMongoStep() {
        return new StepBuilder("cleanMongoStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    commentRepository.deleteAll();
                    bookRepository.deleteAll();
                    authorRepository.deleteAll();
                    genreRepository.deleteAll();
                    return RepeatStatus.FINISHED;
                }, platformTransactionManager)
                .build();
    }
}
