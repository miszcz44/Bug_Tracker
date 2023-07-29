package com.mcr.bugtracker.BugTrackerApplication.project;

import java.util.ArrayList;
import java.util.List;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketForProjectViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;
    private final TicketService ticketService;

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

    public ProjectDetailsViewDto getDataForProjectDetailsView(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Project projectWithDemandedFields = new Project.Builder()
                .id(projectId)
                .name(project.getName())
                .description(project.getDescription())
                .build();
        List<TicketForProjectViewDto> tickets = ticketService.getDemandedTicketDataForProjectView(project.getTickets());
        List<AppUser> projectPersonnelWithDemandedData =
                appUserService.getDemandedPersonnelDataForProjectView(project.getProjectPersonnel());
        return(new ProjectDetailsViewDto(projectWithDemandedFields, projectPersonnelWithDemandedData, tickets));
    }

    public ProjectResponseDto getDataForProjectResponse(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        Project projectWithDemandedFields = new Project.Builder()
                .name(project.getName())
                .description(project.getDescription())
                .build();
        AppUser currentManagerWithDemandedData = new AppUser.Builder()
                .wholeName(project.getProjectManager().getWholeName())
                .email(project.getProjectManager().getEmail())
                .build();
        List<AppUser> projectManagersWithDemandedData =
                appUserService.findProjectManagersParticipatingInProject(project.getProjectPersonnel());
        projectManagersWithDemandedData.add(currentManagerWithDemandedData);
        List<AppUser> projectPersonnelWithDemandedData =
                retrieveDemandedDataFromUsersForProjectView(project.getProjectPersonnel());
        List<AppUser> allUsersNotInProject =
                appUserService.getAllUsersNotParticipatingInProject(project.getProjectPersonnel(), project.getProjectManager());
        List<AppUser> allUsersNotInProjectWithDemandedData =
                retrieveDemandedDataFromUsersForProjectView(allUsersNotInProject);
        return new ProjectResponseDto(projectWithDemandedFields,
                currentManagerWithDemandedData,
                projectManagersWithDemandedData,
                projectPersonnelWithDemandedData,
                allUsersNotInProjectWithDemandedData);
    }

    private List<AppUser> retrieveDemandedDataFromUsersForProjectView(List<AppUser> projectPersonnel) {
        List<AppUser> projectPersonnelWithDemandedData = new ArrayList<>();
        for(AppUser appUser : projectPersonnel) {
            AppUser appUserWithDemandedData = new AppUser.Builder()
                    .id(appUser.getId())
                    .email(appUser.getEmail())
                    .sRole(appUser.getSRole())
                    .wholeName(appUser.getWholeName())
                    .build();
            projectPersonnelWithDemandedData.add(appUserWithDemandedData);
        }
        return projectPersonnelWithDemandedData;
    }
}
