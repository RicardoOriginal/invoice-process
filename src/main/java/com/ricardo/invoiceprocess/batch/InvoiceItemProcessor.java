package com.ricardo.invoiceprocess.batch;

import com.ricardo.invoiceprocess.dto.InvoiceFile;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class InvoiceItemProcessor implements ItemProcessor<InvoiceFile, InvoiceFile> {

    private final JavaMailSender mailSender;

    @Value("sender")
    private String sender;

    @Value("fileOutput")
    private String fileOutput;

    @Override
    public InvoiceFile process(final InvoiceFile invoiceFile) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Map<String, Object> model = new HashMap<>();
        model.put("name", invoiceFile.getName());
        model.put("code", invoiceFile.getDebtUuid());
        helper.setFrom(sender);
        helper.setTo(invoiceFile.getEmail());
        helper.setCc(sender);

        FileSystemResource file = new FileSystemResource(fileOutput);
        helper.addAttachment(file.getFilename(), file);

        log.info("Send email: for = {}, Debit = {}",invoiceFile.getEmail(), invoiceFile.getDebtUuid());
        return null;
    }
}