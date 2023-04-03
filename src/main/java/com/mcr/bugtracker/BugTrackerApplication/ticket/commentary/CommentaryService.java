package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentaryService {

    private final CommentaryRepository commentaryRepository;
    public void saveCommentary(CommentaryRequest request) {
        commentaryRepository.save(new Commentary(request.getMessage()));
    }
}
