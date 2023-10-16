package com.mcr.bugtracker.BugTrackerApplication.project;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserDto;

import java.util.List;

public class ProjectEditViewDto {
    private ProjectDto project;
    private AppUserDto currentManager;
    private List<AppUserDto> projectManagers;
    private List<AppUserDto> projectPersonnel;
    private List<AppUserDto> allUsersNotInProject;

    public ProjectEditViewDto(ProjectDto project, AppUserDto manager, List<AppUserDto> projectManagers, List<AppUserDto> projectPersonnel, List<AppUserDto> allUsersNotInProject) {
        this.project = project;
        this.currentManager = manager;
        this.projectManagers = projectManagers;
        this.projectPersonnel = projectPersonnel;
        this.allUsersNotInProject = allUsersNotInProject;
    }
    public ProjectDto getProject() {
        return project;
    }

    public void setProject(ProjectDto project) {
        this.project = project;
    }

    public AppUserDto getCurrentManager() {
        return currentManager;
    }

    public void setCurrentManager(AppUserDto currentManager) {
        this.currentManager = currentManager;
    }

    public List<AppUserDto> getProjectManagers() {
        return projectManagers;
    }

    public void setProjectManagers(List<AppUserDto> projectManagers) {
        this.projectManagers = projectManagers;
    }

    public List<AppUserDto> getProjectPersonnel() {
        return projectPersonnel;
    }

    public void setProjectPersonnel(List<AppUserDto> projectPersonnel) {
        this.projectPersonnel = projectPersonnel;
    }

    public List<AppUserDto> getAllUsersNotInProject() {
        return allUsersNotInProject;
    }

    public void setAllUsersNotInProject(List<AppUserDto> allUsersNotInProject) {
        this.allUsersNotInProject = allUsersNotInProject;
    }
}
