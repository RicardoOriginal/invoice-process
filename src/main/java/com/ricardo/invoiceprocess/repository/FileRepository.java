package com.ricardo.invoiceprocess.repository;

import com.ricardo.invoiceprocess.entity.File;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository  extends CrudRepository<File, Long>, JpaSpecificationExecutor<File> {
    Optional<File> findByFileName(String fileName);
}
