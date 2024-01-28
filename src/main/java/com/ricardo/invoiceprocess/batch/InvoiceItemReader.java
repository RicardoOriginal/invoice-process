package com.ricardo.invoiceprocess.batch;

import com.ricardo.invoiceprocess.dto.InvoiceFile;
import com.ricardo.invoiceprocess.util.FileDirectoryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class InvoiceItemReader{

    private final FileDirectoryHelper fileDirectoryHelper;

    @StepScope
    @Bean(name = "InvoiceFileReader")
    public FlatFileItemReader reader(@Value("#{jobParameters['fileName']}") String fileName) {
        var filePath = fileDirectoryHelper.getFilePathForProcessing() + fileName + ".csv";
        return new FlatFileItemReaderBuilder()
                .name("InvoiceFileReader")
                .resource(new PathResource(filePath))
                .delimited()
                .names(new String[]{"name", "governmentId", "email", "debtAmount", "debtDueDate", "debtId"})
                .linesToSkip(1)
                .fieldSetMapper(fieldSet -> {

                    var name = fieldSet.readString(0).replaceAll("\\|", "");
                    var governmentId = fieldSet.readLong(1);
                    var email = fieldSet.readString(2).replaceAll("\\|", "");
                    var debtAmount = fieldSet.readBigDecimal(3);
                    var debtDueDate = fieldSet.readDate(4);
                    var debtUuid = UUID.fromString(fieldSet.readString(5).replaceAll("\\|", ""));

                    return InvoiceFile.builder()
                            .name(name)
                            .governmentId(governmentId)
                            .email(email)
                            .debtAmount(debtAmount)
                            .debtDueDate(debtDueDate)
                            .debtUuid(debtUuid)
                            .build();
                })
                .build();
    }
}