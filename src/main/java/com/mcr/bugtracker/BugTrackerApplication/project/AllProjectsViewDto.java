package com.mcr.bugtracker.BugTrackerApplication.project;

public class AllProjectsViewDto {
    private Long id;
    private String name;
    private String description;
    private String projectManagerEmail;

    public AllProjectsViewDto(Long id, String name, String description, String projectManagerEmail) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.projectManagerEmail = projectManagerEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectManagerEmail() {
        return projectManagerEmail;
    }

    public void setProjectManagerEmail(String projectManagerEmail) {
        this.projectManagerEmail = projectManagerEmail;
    }
}
