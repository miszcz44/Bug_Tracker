package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    public void saveAttachment(AttachmentRequest attachmentRequest) {
        attachmentRepository.save(new Attachment(attachmentRequest.getFile(),
                                    attachmentRequest.getNotes()));
    }
}
