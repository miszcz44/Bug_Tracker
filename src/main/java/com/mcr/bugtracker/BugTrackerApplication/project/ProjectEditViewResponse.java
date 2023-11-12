package com.mcr.bugtracker.BugTrackerApplication.project;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Setter
public class ProjectEditViewResponse {
    private Project project;
    private AppUser currentManager;
    private List<AppUser> projectPersonnel;

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
