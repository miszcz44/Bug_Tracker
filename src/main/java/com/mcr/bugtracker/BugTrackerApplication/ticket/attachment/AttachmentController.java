package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment;

import com.google.cloud.storage.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping(path = "api/v1/attachment")
@AllArgsConstructor
@Slf4j
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final Storage storage = StorageOptions.getDefaultInstance().getService();
//    private final Bucket bucket = storage.create(BucketInfo.of("adfsga"));
//    private final String value = "Hello, World!";
//    private final byte[] bytes = value.getBytes(UTF_8);
//    private final Blob blob = bucket.create("my-first-blob", bytes);

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<FileUploadResponse> uploadFiles(@RequestParam("files") MultipartFile[] files,
                                                          @RequestParam("notes") String notes) {
        attachmentService.uploadFilesToGCS(files);
        try {
            createDirIfNotExist();
            log.info(notes);
            List<String> fileNames = new ArrayList<>();

            // read and write the file to the local folder
            Arrays.asList(files).stream().forEach(file -> {
                byte[] bytes = new byte[0];
                try {
                    fileNames.add(file.getOriginalFilename());
                    bytes = file.getBytes();
                    log.info(bytes.toString());
                    Files.write(Paths.get("uploadedFiles/" + file.getOriginalFilename()), bytes);
                } catch (IOException e) {

                }
            });
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new FileUploadResponse("Files uploaded successfully: " + fileNames));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new FileUploadResponse("Exception to upload files!"));
        }
    }

    /**
     * Create directory to save files, if not exist
     */
    private void createDirIfNotExist() {
        //create directory to save the files
        File directory = new File("/");
        if (! directory.exists()){
            directory.mkdir();
        }
    }
}
