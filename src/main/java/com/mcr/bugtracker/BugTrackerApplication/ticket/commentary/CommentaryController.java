package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/comments")
@AllArgsConstructor
@Slf4j
public class CommentaryController {

    private final CommentaryService commentaryService;
    @PostMapping("{ticketId}")
    public void saveComment(@RequestBody CommentsForTicketDetailsViewDto comment, @PathVariable Long ticketId) {
        commentaryService.saveComment(comment, ticketId);
    }
}
