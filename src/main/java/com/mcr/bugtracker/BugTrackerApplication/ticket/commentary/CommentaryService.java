package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CommentaryService {

    private final CommentaryRepository commentaryRepository;
    private final TicketRepository ticketRepository;
    private final AppUserRepository appUserRepository;
    public Commentary saveCommentary(CommentaryRequest request) {
        log.info(request.toString());
        Ticket ticket = ticketRepository.findById(request.getTicketId()).orElseThrow();
        Commentary commentary = new Commentary(request.getId(),
                getUserFromContext().orElseThrow(),
                request.getMessage(),
                LocalDateTime.now(),
                ticket);

        commentaryRepository.save(commentary);
        return commentary;
    }

    public Optional<AppUser> getUserFromContext() {
        AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUserRepository.findById(user.getId());
    }

    public List<Commentary> findAllCommentsByTicketId(Long ticketId) {
        return commentaryRepository.findAllCommentsByTicketId(ticketId);
    }

    public Commentary updateCommentMessageById(String message, Long commentId) {
        String messageWithoutQuotationMarks = message.substring(1, message.length() - 1);
        Commentary commentary = commentaryRepository.findById(commentId).orElseThrow();
        commentary.setMessage(messageWithoutQuotationMarks);
        return commentaryRepository.save(commentary);
    }
}
