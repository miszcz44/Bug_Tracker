package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment;

import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping(path = "api/v1/attachment")
//@AllArgsConstructor
@Slf4j
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    private final Storage storage = StorageOptions.getDefaultInstance().getService();
//    private final Bucket bucket = storage.create(BucketInfo.of("adfsga"));
//    private final String value = "Hello, World!";
//    private final byte[] bytes = value.getBytes(UTF_8);
//    private final Blob blob = bucket.create("my-first-blob", bytes);
    @Value("gs://adsfga/ticket32/message.txt-ekirad.txt")
    private Resource gcsFile; // to delete
    @Value("${gcp.bucket.id}")
    private String bucketName;

    @GetMapping("/test")
    public List<String> listOfFiles() {

        List<String> list = new ArrayList<>();
        Page<Blob> blobs = storage.list(bucketName);
        for (Blob blob : blobs.iterateAll()) {
            list.add(blob.getName());
        }
        return list;
    }

    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public void uploadFiles(@RequestParam("files") MultipartFile[] files,
                            @RequestParam("notes") String notes,
                            @RequestParam("ticketId") Long ticketId,
                            @RequestParam("email") String email) {
//        Blob blob = storage.get()
//        ByteArrayResource resource = new ByteArrayResource(
//                blob.getContent());
//        log.info(resource.toString());
        attachmentService.uploadFilesToGCS(files, ticketId);
        Arrays.asList(files).forEach(file -> {
            String originalFileName = file.getOriginalFilename();
            attachmentService.saveAttachment(originalFileName, notes, email, ticketId);
        });

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
