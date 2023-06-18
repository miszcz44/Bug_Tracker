package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment.file;


import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class FileUploadService {

    public FileUploadService() throws IOException {};
    private FileDetailsRepository fileDetailsRepository;

    private Path UPLOAD_PATH;
    {
        try {
            UPLOAD_PATH = Paths.get(new ClassPathResource("").getFile().getAbsolutePath() + File.separator + "static"  + File.separator + "image");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public FileUploadResponse uploadFile(MultipartFile file) throws IOException {

        if (!Files.exists(UPLOAD_PATH)) {
            Files.createDirectories(UPLOAD_PATH);
        }


        String timeStampedFileName = new SimpleDateFormat("ssmmHHddMMyyyy")
                .format(new Date()) + "_" + file.getOriginalFilename();

        Path filePath = UPLOAD_PATH.resolve(timeStampedFileName);
        Files.copy(file.getInputStream(), filePath);

        String fileUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/image/").path(timeStampedFileName).toUriString();

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/file/download/").path(timeStampedFileName).toUriString();

        FileDetails fileDetails = new FileDetails(file.getOriginalFilename(), fileUri, fileDownloadUri, file.getSize());

        this.fileDetailsRepository.save(fileDetails);

        FileUploadResponse fileUploadResponse =
                new FileUploadResponse(fileDetails.getId(),
                        file.getOriginalFilename(), fileUri, fileDownloadUri,
                        file.getSize());

        return fileUploadResponse;
    }

    public Resource fetchFileAsResource(String fileName) throws FileNotFoundException {

        try {
            Path filePath = UPLOAD_PATH.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName);
        }
    }

    public List<FileDetails> getAllFiles() {
        return this.fileDetailsRepository.findAll();
    }
}
