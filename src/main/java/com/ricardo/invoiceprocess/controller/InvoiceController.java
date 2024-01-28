package com.ricardo.invoiceprocess.controller;

import com.ricardo.invoiceprocess.dto.FileDTO;
import com.ricardo.invoiceprocess.dto.ResponseDTO;
import com.ricardo.invoiceprocess.service.JobProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InvoiceController {

    private final JobProcessorService jobProcessorService;

    @PostMapping("/process")
    public ResponseDTO process(@RequestHeader("traceLogId") long traceLogId) {

        jobProcessorService.process(null);

        return ResponseDTO.builder()
                .code("200")
                .msg("Successfully")
                .build();
    }

    @PostMapping("/process-file")
    public ResponseDTO processFile(
            @RequestHeader("traceLogId") long traceLogId,
            @RequestParam(value = "file", required = true) MultipartFile file) throws IOException {

        jobProcessorService.processFile(file);

        return ResponseDTO.builder()
                .code("200")
                .msg("Successfully")
                .build();
    }

    @GetMapping("/process-file")
    public List<FileDTO> getFileList(@RequestHeader("traceLogId") long traceLogId) {
        return jobProcessorService.getFileList();
    }
}
