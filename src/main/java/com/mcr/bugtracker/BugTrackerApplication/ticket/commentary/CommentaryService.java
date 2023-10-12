package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketRepository;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class CommentaryService {

    private final CommentaryRepository commentaryRepository;
    private final TicketRepository ticketRepository;
    private final AppUserRepository appUserRepository;

    public List<Commentary> findAllCommentsByTicketId(Long ticketId) {
        return commentaryRepository.findAllCommentsByTicketId(ticketId);
    }

    public Commentary updateCommentMessageById(String message, Long commentId) {
        String messageWithoutQuotationMarks = message.substring(1, message.length() - 1);
        Commentary commentary = commentaryRepository.findById(commentId).orElseThrow();
        commentary.setMessage(messageWithoutQuotationMarks);
        return commentaryRepository.save(commentary);
    }

    public void deleteCommentById(Long commentId) {
        commentaryRepository.deleteById(commentId);
    }

    public List<CommentsForTicketDetailsViewDto> getCommentsWithDemandedData(List <Commentary> comments) {
        List<CommentsForTicketDetailsViewDto> commentsWithDemandedData = new ArrayList<>();
        for(Commentary comment : comments) {
            commentsWithDemandedData.add(
                    new CommentsForTicketDetailsViewDto(comment.getId(),
                            comment.getCommentator().getEmail(),
                            comment.getMessage(),
                            comment.getCreatedAt().truncatedTo(ChronoUnit.SECONDS)));
        }
        return commentsWithDemandedData;
    }

    public void saveComment(CommentsForTicketDetailsViewDto comment, Long ticketId) {
        Commentary commentForDb = new Commentary(appUserRepository.findByEmail(comment.getCommentatorEmail()).orElseThrow(),
                comment.getMessage(),
                LocalDateTime.now(),
                ticketRepository.findById(ticketId).orElseThrow());
        commentaryRepository.save(commentForDb);
    }
}
