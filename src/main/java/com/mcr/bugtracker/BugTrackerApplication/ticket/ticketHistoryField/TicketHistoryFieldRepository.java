package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketHistoryFieldRepository extends JpaRepository<TicketHistoryField, Long> {

    @Query(value = "SELECT * FROM ticket_history_field WHERE ticket_id = ?1",
            nativeQuery = true)
    List<TicketHistoryField> findAllByTicketId(Long id);
}
