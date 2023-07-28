package com.mcr.bugtracker.BugTrackerApplication.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    @Query(value = "SELECT * FROM ticket WHERE project_id = ?1",
            nativeQuery = true)
    List<Ticket> findAllByProjectId(Long projectId);
}
