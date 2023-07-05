package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment.file;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDto {

    private String fileName;
    private String fileUrl;
}