package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment.file;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "file_details")
@EqualsAndHashCode
@Data
@NoArgsConstructor
public class File {

    @Id
    @GeneratedValue
    private Long id;
    private String fileName;
    private String fileUrl;

    public File(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }
}