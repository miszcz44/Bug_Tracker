package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.ticket.attachment.Attachment;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.Commentary;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory.TicketHistory;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Ticket {

    @SequenceGenerator(
            name = "ticket_sequence",
            sequenceName = "ticket_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ticket_sequence"
    )
    private Long id;
    private String title;
    private String description;
    @ManyToOne
    private AppUser submitter;
    @ManyToOne
    private AppUser assignedDeveloper;
    @ManyToOne
    private Project project;
    @OneToMany
    private List<Commentary> comments;
    @OneToMany
    private List<Attachment> attachments;
    @OneToOne
    private TicketHistory history;
    private Priority priority;
    private ProgressStatus status;
    private Type type;
    private LocalDateTime createdAt;

}
