package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField;

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
public class TicketHistoryField {

    @SequenceGenerator(
            name = "ticket_history_field_sequence",
            sequenceName = "ticket_history_field_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ticket_history_field_sequence"
    )
    private Long id;
    private String property;
    private String oldValue;
    private String newValue;
    private LocalDateTime dateChanged;
    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    public TicketHistoryField(String property, String oldValue, String newValue, Ticket ticket, LocalDateTime dateChanged) {
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.ticket = ticket;
        this.dateChanged = dateChanged;
    }
}
