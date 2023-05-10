package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.ticket.attachment.Attachment;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.Commentary;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Priority;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.ProgressStatus;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Type;
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
    @JoinColumn(name = "submitter_id")
    private AppUser submitter;
    @ManyToOne
    @JoinColumn(name = "developer_id")
    private AppUser assignedDeveloper;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @OneToMany(mappedBy = "ticket")
    private List<Commentary> comments;
    @OneToMany(mappedBy = "ticket")
    private List<Attachment> attachments;
    @OneToOne
    @JoinColumn(name = "ticket_history_id")
    private TicketHistory history;
    private String priority;
    private String status;
    private String type;
    private LocalDateTime createdAt;

    public Ticket(String title, String description, String priority, String status, String type, TicketHistory ticketHistory) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.type = type;
        this.history = ticketHistory;
    }

    public Ticket(Project project) {
        this.project = project;
    }
}
