
package com.ricardo.invoiceprocess.batch;

import com.ricardo.invoiceprocess.entity.File;
import com.ricardo.invoiceprocess.repository.FileRepository;
import com.ricardo.invoiceprocess.util.FileDirectoryHelper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobCompletionNotificationListener.class);
    private final FileDirectoryHelper fileDirectoryHelper;
    private final FileRepository fileRepository;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LOGGER.info("!!! JOB FINISHED! Time to verify the results");
            try {
                var filePathForProcessed = fileDirectoryHelper.getFilePathForProcessing();
                var fileName = jobExecution.getJobParameters().getString("fileName");
                var path = Path.of(filePathForProcessed + fileName + ".csv");

                fileDirectoryHelper.moveFirstToSuccessDirectory(path);

                Optional<File> optFile = fileRepository.findByFileName(fileName);
                optFile.ifPresent(file -> {
                    file.setStatus("PROCESSED");
                    file.setUpdateDateTime(LocalDateTime.now());
                    fileRepository.save(file);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}