package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AttachmentRequest {

    private final MultipartFile file;
    private final String notes;
    private final Long ticketId;
}
