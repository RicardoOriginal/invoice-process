package com.ricardo.invoiceprocess.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceFile {
    private Long id;
    private String name;
    private Long governmentId;
    private String email;
    private BigDecimal debtAmount;
    private Date debtDueDate;
    private UUID debtUuid;
}
