package ru.otus.hw.commands;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class CommonCommands {

    private final Job migrateDataToMongoJob;

    private final Job cleanMongoJob;

    private final JobLauncher jobLauncher;

    public CommonCommands(@Qualifier("migrateDataToMongoJob") Job migrateDataToMongoJob,
                          @Qualifier("cleanMongoJob") Job cleanMongoJob,
                          JobLauncher jobLauncher) {
        this.migrateDataToMongoJob = migrateDataToMongoJob;
        this.cleanMongoJob = cleanMongoJob;
        this.jobLauncher = jobLauncher;
    }

    @ShellMethod(value = "Run migration from H2 to Mongo", key = "rm")
    public void runMongoMigration() throws Exception {
        jobLauncher.run(migrateDataToMongoJob, new JobParametersBuilder().toJobParameters());
    }

    @ShellMethod(value = "Restart migration from H2 to Mongo", key = "rsm")
    public void restartMongoMigration() throws Exception {
        jobLauncher.run(cleanMongoJob, new JobParametersBuilder().toJobParameters());
        jobLauncher.run(migrateDataToMongoJob, new JobParametersBuilder().toJobParameters());
    }

}
