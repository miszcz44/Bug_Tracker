package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.ticket.attachment.Attachment;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.Commentary;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Priority;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.ProgressStatus;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketFieldsEnums.Type;

import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryField;
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
    @Transient
    private List<Commentary> comments;
    @OneToMany(mappedBy = "ticket")
    @Transient
    private List<Attachment> attachments;
    @OneToMany(mappedBy = "ticket")
    @Transient
    private List<TicketHistoryField> ticketHistoryFields;
    private String priority;
    private String status;
    private String type;
    private LocalDateTime createdAt;

    public Ticket(String title, String description, String priority, String status, String type) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.type = type;
    }

    public boolean isSubmitterNull() {
        return submitter == null;
    }

    public Ticket(Project project) {
        this.project = project;
    }
}
