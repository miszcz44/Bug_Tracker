package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.File;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AttachmentRequest {

    private final File file;
    private final String notes;
}
