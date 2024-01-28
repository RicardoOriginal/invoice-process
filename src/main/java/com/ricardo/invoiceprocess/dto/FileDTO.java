package com.ricardo.invoiceprocess.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileDTO {
    private Long id;
    private String fileName;
    private String status;
    private String creationDateTime;
    private String updateDateTime;
}
