package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/comments")
@AllArgsConstructor
@Slf4j
public class CommentaryController {

    private final CommentaryService commentaryService;
    @GetMapping
    public ResponseEntity<List<Commentary>> getCommentsByTicket(@RequestParam Long ticketId) {
        List<Commentary> commentaries = commentaryService.findAllCommentsByTicketId(ticketId);
        return ResponseEntity.ok(commentaries);
    }

    @PutMapping("{commentId}")
    public ResponseEntity<Commentary> updateCommentMessage(@RequestBody String message, @PathVariable Long commentId) {
        Commentary commentary = commentaryService.updateCommentMessageById(message, commentId);
        return ResponseEntity.ok(commentary);
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentaryService.deleteCommentById(commentId);
        return ResponseEntity.ok("");
    }
    @PostMapping("{ticketId}")
    public void saveComment(@RequestBody CommentsForTicketDetailsViewDto comment, @PathVariable Long ticketId) {
        commentaryService.saveComment(comment, ticketId);
    }
}
