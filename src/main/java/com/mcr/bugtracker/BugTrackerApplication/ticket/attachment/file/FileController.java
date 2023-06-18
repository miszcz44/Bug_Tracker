package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment.file;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/file")
@AllArgsConstructor
public class FileController {

    private FileUploadService fileUploadService;

    @GetMapping
    public List<FileDetails> getAllFiles() {
        return fileUploadService.getAllFiles();
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Object> downloadFile(@PathVariable String fileName, HttpServletRequest request) {

        try {
            Resource resource = this.fileUploadService.fetchFileAsResource(fileName);
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (IOException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping(value = "/upload")
    public ResponseEntity<Object> uploadFiles(@RequestBody MultipartFile file) {

        try {
            FileUploadResponse fileUploadResponse = fileUploadService.uploadFile(file);

            return new ResponseEntity<>(fileUploadResponse, HttpStatus.OK);
        } catch (UncheckedIOException e) {
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
