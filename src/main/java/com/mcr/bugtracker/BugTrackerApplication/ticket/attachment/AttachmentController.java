package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/attachment")
@AllArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping
    public void saveAttachment(@RequestBody AttachmentRequest attachmentRequest) {
        attachmentService.saveAttachment(attachmentRequest);
    }
}
