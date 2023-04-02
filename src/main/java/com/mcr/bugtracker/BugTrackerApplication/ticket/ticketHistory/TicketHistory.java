package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory;

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
public class TicketHistory {

    @SequenceGenerator(
            name = "ticket_history_sequence",
            sequenceName = "ticket_history_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "ticket_history_sequence"
    )
    private Long id;
    private String property;
    private String oldValue;
    private String newValue;
    private LocalDateTime dateChanged;

}
