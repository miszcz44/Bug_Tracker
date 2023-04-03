package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository <Attachment, Long> {
}
