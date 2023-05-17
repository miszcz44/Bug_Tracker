package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, Long> {
    @Query("SELECT c FROM Commentary c WHERE c.ticket.id = :ticketId")
    List<Commentary> findAllCommentsByTicketId(Long ticketId);
}
