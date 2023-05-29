package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository <Attachment, Long> {

    @Query(value = "SELECT * FROM attachment WHERE ticket_id = ?1",
            nativeQuery = true)
    List<Attachment> findAllAttachmentsByTicketId(Long ticketId);
}
