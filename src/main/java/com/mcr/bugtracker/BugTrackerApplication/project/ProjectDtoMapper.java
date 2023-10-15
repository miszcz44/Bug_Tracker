package com.mcr.bugtracker.BugTrackerApplication.project;

import org.springframework.security.core.parameters.P;

import java.util.function.Function;

public class ProjectDtoMapper implements Function<Project, ProjectDto> {
    @Override
    public ProjectDto apply(Project project) {
        return new ProjectDto(project.getId(), project.getName(), project.getDescription());
    }
}
