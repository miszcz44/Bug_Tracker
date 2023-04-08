package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory;

import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory.ticketHistoryField.TicketHistoryField;
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
    @OneToMany(mappedBy = "ticketHistory")
    private List<TicketHistoryField> ticketHistoryField;
    @OneToOne(mappedBy = "history")
    private Ticket ticket;

}
