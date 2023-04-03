package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CommentaryRequest {
    private final String message;
}
