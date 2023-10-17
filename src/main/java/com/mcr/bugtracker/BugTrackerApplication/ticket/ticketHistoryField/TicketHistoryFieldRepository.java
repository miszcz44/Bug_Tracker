package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketHistoryFieldRepository extends JpaRepository<TicketHistoryField, Long> {}
