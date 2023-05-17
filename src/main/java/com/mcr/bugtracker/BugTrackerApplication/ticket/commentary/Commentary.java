package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Commentary {
    @SequenceGenerator(
            name = "commentary_sequence",
            sequenceName = "commentary_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "commentary_sequence"
    )
    private Long id;
    @ManyToOne
    @JoinColumn(name = "commentator_id")
    private AppUser commentator;
    private String message;
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public Commentary(String message) {
        this.message = message;
    }

    public Commentary(Long id, AppUser commentator, String message, LocalDateTime createdAt, Ticket ticket) {
        this.id = id;
        this.commentator = commentator;
        this.message = message;
        this.createdAt = createdAt;
        this.ticket = ticket;
    }
}
