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
import java.util.Optional;

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
    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Commentary> comments;
    @OneToMany(mappedBy = "ticket",
                cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Attachment> attachments;
    @OneToMany(mappedBy = "ticket",
                cascade = CascadeType.ALL)
    @JsonIgnore
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

    public Optional<AppUser> getOptionalSubmitter() {
        return Optional.ofNullable(submitter);
    }

    public Optional<AppUser> getOptionalDeveloper() {
        return Optional.ofNullable(assignedDeveloper);
    }

    public Ticket(Project project) {
        this.project = project;
    }

    public static final class Builder {
        private Long id;
        private String title;
        private String description;
        private String priority;
        private String status;
        private String type;
        private LocalDateTime createdAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder priority(String priority) {
            this.priority = priority;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Ticket build() {
            Ticket ticket = new Ticket();
            ticket.id = this.id;
            ticket.title = this.title;
            ticket.description = this.description;
            ticket.status = this.status;
            ticket.type = this.type;
            ticket.priority = this.priority;
            ticket.createdAt = this.createdAt;
            return ticket;
        }
    }
}
