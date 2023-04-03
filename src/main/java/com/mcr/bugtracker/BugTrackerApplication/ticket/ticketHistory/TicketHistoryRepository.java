package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketHistoryRepository extends JpaRepository<TicketHistory, Long> {
}
