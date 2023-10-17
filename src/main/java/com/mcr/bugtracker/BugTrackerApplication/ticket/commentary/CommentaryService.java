package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CommentaryService {
    private final CommentaryRepository commentaryRepository;
    private final TicketRepository ticketRepository;
    private final AppUserRepository appUserRepository;
    private final CommentForTicketDetailsViewDtoMapper commentForTicketDetailsViewDtoMapper;
    public List<CommentsForTicketDetailsViewDto> getCommentsWithDemandedData(List<Commentary> comments) {
        return comments.stream()
                .map(commentForTicketDetailsViewDtoMapper)
                .collect(Collectors.toList());
    }
    public void saveComment(CommentsForTicketDetailsViewDto comment, Long ticketId) {
        Commentary commentForDb = new Commentary(appUserRepository.findByEmail(comment.getCommentatorEmail()).orElseThrow(),
                comment.getMessage(),
                LocalDateTime.now(),
                ticketRepository.findById(ticketId).orElseThrow());
        commentaryRepository.save(commentForDb);
    }
}
