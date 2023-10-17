package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.function.Function;

@Service
public class CommentForTicketDetailsViewDtoMapper implements Function<Commentary, CommentsForTicketDetailsViewDto> {
    @Override
    public CommentsForTicketDetailsViewDto apply(Commentary commentary) {
        return new CommentsForTicketDetailsViewDto(commentary.getId(),
                commentary.getCommentator().getEmail(),
                commentary.getMessage(),
                commentary.getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
    }
}
