package com.mcr.bugtracker.BugTrackerApplication.project.Mapper;

import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.project.DTO.ProjectDto;
import org.springframework.stereotype.Service;

import java.util.function.Function;
@Service
public class ProjectDtoMapper implements Function<Project, ProjectDto> {
    @Override
    public ProjectDto apply(Project project) {
        return new ProjectDto(project.getId(), project.getName(), project.getDescription());
    }
}
