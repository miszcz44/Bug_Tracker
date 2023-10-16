package com.mcr.bugtracker.BugTrackerApplication.project;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;

import java.util.List;

public class ProjectEditViewResponse {
    private Project project;
    private AppUser currentManager;
    private List<AppUser> projectPersonnel;

    public ProjectEditViewResponse(Project project, AppUser currentManager, List<AppUser> projectPersonnel) {
        this.project = project;
        this.currentManager = currentManager;
        this.projectPersonnel = projectPersonnel;
    }

    public Project getProject() {
        return project;
    }

    public AppUser getCurrentManager() {
        return currentManager;
    }

    public List<AppUser> getProjectPersonnel() {
        return projectPersonnel;
    }
}
