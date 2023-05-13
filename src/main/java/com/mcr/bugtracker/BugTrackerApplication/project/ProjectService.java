package com.mcr.bugtracker.BugTrackerApplication.project;

import java.util.List;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final AppUserRepository appUserRepository;

    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }
    public void addUsersToProject(UsersToProjectRequest request) {
        Project project = projectRepository.findById(request.getProjectId()).get();
        for(Long id : request.getIds()) {
            project.getProjectPersonnel().add(appUserRepository.findById(id).get());
        }
        projectRepository.save(project);
    }
    public Optional<AppUser> getUserFromContext() {
        AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUserRepository.findById(user.getId());
    } // TODO sa 3 takie same klasy w 3 serwisach

    public List<Project> findAllProjectsAssignedToUser() {
        List<Long> projectIds = projectRepository.findAllProjectsIdsAssignedToUser(getUserFromContext().orElseThrow().getId());
        if(projectIds.isEmpty()) {
            return List.of();
        }
        return projectRepository.findByIds(projectIds);
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public List<AppUser> getProjectPersonnel(Long id) {
        return appUserRepository.getProjectPersonnel(id);
    }


    public void addUserToProjectByEmail(Project project, String email) {
        project.getProjectPersonnel().add(appUserRepository.findByEmail(email).orElseThrow());
    }

    public void deleteUserFromProject(Long userId) {
        projectRepository.deleteUserFromProject(userId);
    }
}
