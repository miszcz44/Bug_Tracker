package com.mcr.bugtracker.BugTrackerApplication.ticket.attachment;

import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AttachmentService {

    TicketService ticketService;

    private final AttachmentRepository attachmentRepository;
    public Attachment saveAttachment(AttachmentRequest request) {
        Attachment attachment = new Attachment(request.getFile(),
                                                request.getNotes());
        attachment.setTicket(ticketService.findById(request.getTicketId()).orElseThrow());
        return attachmentRepository.save(attachment);
    }

    public List<Attachment> getAllAttachmentsByTicketId(Long ticketId) {
        return attachmentRepository.findAllAttachmentsByTicketId(ticketId);
    }
}
