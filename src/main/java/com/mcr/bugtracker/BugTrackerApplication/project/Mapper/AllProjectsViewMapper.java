package com.mcr.bugtracker.BugTrackerApplication.project.Mapper;

import com.mcr.bugtracker.BugTrackerApplication.project.DTO.AllProjectsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class AllProjectsViewMapper implements Function<Project, AllProjectsViewDto> {
    @Override
    public AllProjectsViewDto apply(Project project) {
        return new AllProjectsViewDto(
                project.getId(), project.getName(), project.getDescription(), project.getProjectManager().getEmail());
    }
}
