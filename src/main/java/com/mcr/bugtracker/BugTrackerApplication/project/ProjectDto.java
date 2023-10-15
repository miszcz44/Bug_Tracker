package com.mcr.bugtracker.BugTrackerApplication.project;

public class ProjectDto {
    private Long id;
    private String name;
    private String description;

    public ProjectDto(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
