package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import com.mcr.bugtracker.BugTrackerApplication.util.DateAndTimeFormatter;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CommentForTicketDetailsViewDtoMapper implements Function<Commentary, CommentsForTicketDetailsViewDto> {
    @Override
    public CommentsForTicketDetailsViewDto apply(Commentary commentary) {
        return new CommentsForTicketDetailsViewDto(commentary.getId(),
                commentary.getCommentator().getEmail(),
                commentary.getMessage(),
                DateAndTimeFormatter.convertDateAndTimeToCurrentUserZone(commentary.getCreatedAt()));
    }
}
