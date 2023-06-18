package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Attachment {
    @SequenceGenerator(
            name = "attachment_sequence",
            sequenceName = "attachment_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "attachment_sequence"
    )
    private Long id;
    private File file;
    @ManyToOne
    @JoinColumn(name = "uploader_id")
    private AppUser uploader;
    private String notes;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public Attachment(File file, String notes) {
        this.file = file;
        this.notes = notes;
    }
}
