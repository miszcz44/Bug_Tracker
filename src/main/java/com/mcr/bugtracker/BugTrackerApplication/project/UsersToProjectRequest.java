package com.mcr.bugtracker.BugTrackerApplication.project;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UsersToProjectRequest {
    private List<Long> ids;
    private Long projectId;
}
