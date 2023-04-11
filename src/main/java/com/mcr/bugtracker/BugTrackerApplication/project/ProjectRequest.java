package com.mcr.bugtracker.BugTrackerApplication.project;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ProjectRequest {
    private String name;
    private String description;
    private Long projectManagerId;

}
