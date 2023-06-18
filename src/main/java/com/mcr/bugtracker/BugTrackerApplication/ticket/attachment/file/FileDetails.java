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
public class FileDetails {

    @SequenceGenerator(
            name = "file_details_sequence",
            sequenceName = "file_details_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "file_details_sequence"
    )
    private int id;


     private String fileName;
     private String fileUri;
     private String fileDownloadUri;
     private long fileSize;
     private String uploaderName;

    public FileDetails(String fileName, String fileUri, String fileDownloadUri, long fileSize) {
        this.fileName = fileName;
        this.fileUri = fileUri;
        this.fileDownloadUri = fileDownloadUri;
        this.fileSize = fileSize;

    }
}