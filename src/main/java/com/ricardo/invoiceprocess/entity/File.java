package com.ricardo.invoiceprocess.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "public", name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String status;
    private LocalDateTime creationDateTime;
    private LocalDateTime updateDateTime;

    @PrePersist
    public void prePersist() {
        creationDateTime = LocalDateTime.now();
    }
}
