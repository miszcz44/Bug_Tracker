package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/commentary")
@AllArgsConstructor
public class CommentaryController {

    private final CommentaryService commentaryService;
    @PostMapping
    public void saveCommentary(@RequestBody CommentaryRequest request) {
        commentaryService.saveCommentary(request);
    }
}
