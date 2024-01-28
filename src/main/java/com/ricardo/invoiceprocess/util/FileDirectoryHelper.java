package com.ricardo.invoiceprocess.util;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@Data
public class FileDirectoryHelper {

    @Value("${file-dir.base-path}")
    private String fileBasePath;

    @Value("${file-dir.bill.in}")
    private String filePathReader;

    @Value("${file-dir.bill.processing}")
    private String filePathInProcessing;

    @Value("${file-dir.bill.processed}")
    private String filePathInProcessed;

    private Path dataDirectory;
    private Path dataSuccessDirectory;
    private Path dataErrorDirectory;

    @PostConstruct
    private void postConstruct() {
        dataDirectory = Paths.get(fileBasePath + filePathReader);
        dataSuccessDirectory = Paths.get(fileBasePath + filePathInProcessed);
    }

    public Optional<Path> getPathOfFirstFile() throws IOException {
        List<Path> filesSorted = getFilePathSorted();
        if (!CollectionUtils.isEmpty(filesSorted)) {
            return filesSorted.stream().findFirst();
        }
        return Optional.empty();
    }

    public String getNameOfFirstFile() throws IOException {
        Path pathOfFirstFile = getPathOfFirstFile().orElse(null);
        if (pathOfFirstFile != null) {
            return pathOfFirstFile.toString();
        }
        return null;
    }

    public List<Path> getFilePathSorted() throws IOException {
        try (Stream<Path> pathStream = Files.list(dataDirectory)) {
            return pathStream.filter(path -> path.toFile().isFile()).collect(Collectors.toList());
        } catch (IOException e) {
            throw new IOException("Houve um erro ao buscar o arquivo no diretório informado: " + e.getMessage());
        }
    }

    public void moveFirstToSuccessDirectory(Path source) throws IOException {
        log.debug("Operation moveFirstToSuccessDirectory: {}", source);
        Path successDirectory = dataSuccessDirectory;
        if (source != null) {
            Files.move(source, successDirectory.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void moveFirstToErrorDirectory(Path source) throws IOException {
        log.debug("Operation moveFirstToErrorDirectory: {}", source);
        Path successDirectory = dataErrorDirectory;
        if (source != null) {
            Files.move(source, successDirectory.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public String getFilePathForProcessing() {
        return this.getFileBasePath() + this.getFilePathReader();
    }

    public String getFilePathForProcessed() {
        return this.getFileBasePath() + this.getFilePathInProcessed();
    }

    public void writeFile(byte[] bytes, String filename) throws IOException {
        Path pathData = Paths.get(fileBasePath + filePathReader + filename + ".csv");
        Files.write(pathData, bytes, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
}