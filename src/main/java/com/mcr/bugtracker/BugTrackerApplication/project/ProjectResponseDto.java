package com.mcr.bugtracker.BugTrackerApplication.project;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import java.util.List;

public class ProjectResponseDto {
    private Project project;
    private AppUser currentManager;
    private List<AppUser> projectManagers;
    private List<AppUser> projectPersonnel;
    private List<AppUser> allUsersNotInProject;

    public ProjectResponseDto(Project project, AppUser manager, List<AppUser> projectManagers, List<AppUser> projectPersonnel, List<AppUser> allUsersNotInProject) {
        this.project = project;
        this.currentManager = manager;
        this.projectManagers = projectManagers;
        this.projectPersonnel = projectPersonnel;
        this.allUsersNotInProject = allUsersNotInProject;
    }
    public ProjectResponseDto(Project project, AppUser manager, List<AppUser> projectPersonnel) {
        this.project = project;
        this.currentManager = manager;
        this.projectPersonnel = projectPersonnel;
    }

    public ProjectResponseDto() {}


    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public AppUser getCurrentManager() {
        return currentManager;
    }

    public void setCurrentManager(AppUser currentManager) {
        this.currentManager = currentManager;
    }

    public List<AppUser> getProjectManagers() {
        return projectManagers;
    }

    public void setProjectManagers(List<AppUser> projectManagers) {
        this.projectManagers = projectManagers;
    }

    public List<AppUser> getProjectPersonnel() {
        return projectPersonnel;
    }

    public void setProjectPersonnel(List<AppUser> projectPersonnel) {
        this.projectPersonnel = projectPersonnel;
    }

    public List<AppUser> getAllUsersNotInProject() {
        return allUsersNotInProject;
    }

    public void setAllUsersNotInProject(List<AppUser> allUsersNotInProject) {
        this.allUsersNotInProject = allUsersNotInProject;
    }
}
