package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/attachment")
@AllArgsConstructor
@Slf4j
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping
    public ResponseEntity<?> saveAttachment(@RequestBody AttachmentRequest attachment) {
       return ResponseEntity.ok(attachmentService.saveAttachment(attachment));
    }
}
