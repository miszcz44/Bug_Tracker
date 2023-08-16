package com.mcr.bugtracker.BugTrackerApplication.project;

import java.util.ArrayList;
import java.util.List;

import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiForbiddenException;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiNotFoundException;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketForProjectViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Slf4j
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;
    private final TicketService ticketService;
    private final AllProjectsViewMapper allProjectsViewMapper;

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

    public List<AllProjectsViewDto> findAllProjectsAssignedToUser() {
        AppUser user = getUserFromContext().orElseThrow();
        List<Project> projects;
        if(user.getSRole().equals("Admin")) {
            projects = projectRepository.findAll();
        }
        else {
            projects = Stream.concat(user.getAssignedProjects().stream(),
                user.getManagedProjects().stream()).toList();
        }
        List<AllProjectsViewDto> projectsForAllProjectsView = new ArrayList<>();
        for(Project project : projects) {
            projectsForAllProjectsView.add(allProjectsViewMapper.apply(project));
        }
        return projectsForAllProjectsView;
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
        validateUserPermissionForProjectDetails(project);
        Project projectWithDemandedFields = new Project.Builder()
                .id(projectId)
                .name(project.getName())
                .description(project.getDescription())
                .build();
        List<TicketForProjectViewDto> tickets = ticketService.getDemandedTicketDataForProjectView(project.getTickets());
        List<AppUser> projectPersonnelWithDemandedData =
                appUserService.getDemandedPersonnelDataForProjectView(project.getProjectPersonnel());
        return new ProjectDetailsViewDto(
                projectWithDemandedFields, project.getProjectManager().getWholeName(), project.getProjectManager().getEmail(), projectPersonnelWithDemandedData, tickets);
    }

    public ProjectResponseDto getDataForProjectResponse(Long projectId) {
        validateProjectExistence(projectId);
        Project project = projectRepository.findById(projectId).get();
        Project projectWithDemandedFields = new Project.Builder()
                .id(projectId)
                .name(project.getName())
                .description(project.getDescription())
                .build();
        AppUser currentManagerWithDemandedData = new AppUser.Builder()
                .id(project.getProjectManager().getId())
                .wholeName(project.getProjectManager().getWholeName())
                .email(project.getProjectManager().getEmail())
                .sRole(project.getProjectManager().getSRole())
                .build();
        validateUserPermissionForProjectEdit(currentManagerWithDemandedData.getId());
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

    private void validateUserPermissionForProjectEdit(Long managerId) {
        AppUser currentUser = getUserFromContext().orElseThrow();
        if (!currentUser.getId().equals(managerId) && !currentUser.getSRole().equals("Admin")) {
            throw new ApiForbiddenException("You do not have permission for this request");
        }
    }

    private void validateProjectExistence(Long projectId) {
        if(!projectRepository.findById(projectId).isPresent()) {
            throw new ApiNotFoundException("There is no such resource");
        }
    }

    private void validateUserPermissionForProjectDetails(Project project) {
        AppUser currentUser = getUserFromContext().orElseThrow();
        if(!project.getProjectPersonnel().contains(currentUser) && !project.getProjectManager().equals(currentUser)) {
            throw new ApiForbiddenException("You do not have permission for this request");
        }
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

    public void saveResponseElements(ProjectResponseDto projectResponse) {
        Project project = projectResponse.getProject();
        validateProjectExistence(project.getId());
        validateUserPermissionForProjectEdit(projectResponse.getCurrentManager().getId());
        project.setProjectManager(appUserService.findById(projectResponse.getCurrentManager().getId()));
        project.setProjectPersonnel(projectResponse.getProjectPersonnel());
        projectRepository.save(project);
    }

    public void deleteProjectById(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        validateUserPermissionForProjectDetails(project);
        projectRepository.deleteById(projectId);
    }

    public Project getProjectForTicketEditView(Project project) {
        return new Project.Builder()
                .id(project.getId())
                .name(project.getName())
                .build();
    }


}
