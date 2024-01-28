package com.ricardo.invoiceprocess.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {
    private String code;
    private String msg;
    private Object returnObject;
}
