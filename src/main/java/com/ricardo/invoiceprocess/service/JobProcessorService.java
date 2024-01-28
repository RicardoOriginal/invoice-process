package com.ricardo.invoiceprocess.service;

import com.ricardo.invoiceprocess.dto.FileDTO;
import com.ricardo.invoiceprocess.entity.File;
import com.ricardo.invoiceprocess.repository.FileRepository;
import com.ricardo.invoiceprocess.util.FileDirectoryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.file.transform.FlatFileFormatException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobProcessorService {

    private final JobLauncher jobLauncher;
    private final Job job;
    private final FileDirectoryHelper fileDirectoryHelper;
    private final FileRepository fileRepository;

    public void process(String fileName){

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .addString("fileName", fileName == null ? "input.csv" : fileName)
                .toJobParameters();
        try {
            startJob(jobParameters);
        } catch (FlatFileFormatException e) {
            log.debug("erro");
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }

    @Async
    ResponseEntity<String> startJob(JobParameters jobParameters) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
        JobExecution run = jobLauncher.run(job, jobParameters);
        return new ResponseEntity<>(ExitStatus.COMPLETED.getExitCode(), HttpStatus.OK);
    }

    public void processFile(MultipartFile multipartFile) throws IOException {
        log.info("Check if file name exists.");
        var fileName = "input_" + System.currentTimeMillis();
        fileDirectoryHelper.writeFile(multipartFile.getBytes(), fileName);

        var file = File.builder()
                .fileName(fileName)
                .status("PROCESSING")
                .build();

        fileRepository.save(file);
        this.process(fileName);
    }

    public List<FileDTO> getFileList() {
        List<FileDTO> fileDTOList = new ArrayList<>();
        var all = fileRepository.findAll();
        all.forEach(file -> {
            var fileDTO = FileDTO.builder()
                    .id(file.getId())
                    .fileName(file.getFileName())
                    .status(file.getStatus())
                    .creationDateTime(formatDate(file.getCreationDateTime()))
                    .updateDateTime(formatDate(file.getUpdateDateTime()))
                    .build();
            fileDTOList.add(fileDTO);
        });
        return fileDTOList;
    }

    private String formatDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(dateTimeFormatter);
    }
}
