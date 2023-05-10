package com.mcr.bugtracker.BugTrackerApplication.project;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import java.util.List;

public class ProjectResponseDto {
    private Project project;
    private String managerName;
    private List<AppUser> projectPersonnel;
    private List<AppUser> allUsers;

    public ProjectResponseDto(Project project, String managerName, List<AppUser> projectPersonnel, List<AppUser> allUsers) {
        this.project = project;
        this.managerName = managerName;
        this.projectPersonnel = projectPersonnel;
        this.allUsers = allUsers;
    }

    public List<AppUser> getProjectPersonnel() {
        return projectPersonnel;
    }

    public void setProjectPersonnel(List<AppUser> projectPersonnel) {
        this.projectPersonnel = projectPersonnel;
    }

    public List<AppUser> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<AppUser> allUsers) {
        this.allUsers = allUsers;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

}
