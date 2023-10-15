package com.mcr.bugtracker.BugTrackerApplication.project;

import java.util.List;

import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiForbiddenException;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiNotFoundException;
import com.mcr.bugtracker.BugTrackerApplication.appuser.*;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketForProjectViewDto;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
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
    private final AppUserDtoMapper appUserDtoMapper;
    private final ProjectDtoMapper projectDtoMapper;

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
    public List<AllProjectsViewDto> findAllProjectsAssignedToUser() {
        AppUser user = appUserService.getUserFromContext().orElseThrow();
        if(user.getSRole().equals("Admin") && user.isDemo()) {
            return projectRepository.findAll()
                    .stream()
                    .filter(project -> project.getProjectManager().isDemo())
                    .map(allProjectsViewMapper)
                    .collect(Collectors.toList());
        }
        else if(user.getSRole().equals("Admin")) {
            return projectRepository.findAll()
                    .stream()
                    .map(allProjectsViewMapper)
                    .collect(Collectors.toList());
        }
        else {
            return Stream.concat(user.getAssignedProjects().stream(),
                user.getManagedProjects().stream())
                    .map(allProjectsViewMapper)
                    .collect(Collectors.toList());
        }
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
        validateProjectExistence(projectId);
        Project project = projectRepository.findById(projectId).get();
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

    public ProjectEditViewDto getDataForProjectEditView(Long projectId) {
        validateProjectExistence(projectId);
        Project project = projectRepository.findById(projectId).orElseThrow();
        validateUserPermissionForProjectEdit(project.getProjectManager().getId());
        return new ProjectEditViewDto(projectDtoMapper.apply(project),
                appUserDtoMapper.apply(project.getProjectManager()),
                project.getProjectPersonnel().stream()
                        .filter(user -> user.getSRole().equals("Project manager"))
                        .map(appUserDtoMapper)
                        .collect(Collectors.toList()),
                project.getProjectPersonnel().stream()
                        .map(appUserDtoMapper)
                        .collect(Collectors.toList()),
                appUserService.getAllUsersNotInProject(project));
    }

    private void validateUserPermissionForProjectEdit(Long managerId) {
        AppUser currentUser = appUserService.getUserFromContext().orElseThrow();
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
        AppUser currentUser = appUserService.getUserFromContext().orElseThrow();
        if(!project.getProjectPersonnel().contains(currentUser) && !currentUser.equals(project.getProjectManager()) &&
            !currentUser.getSRole().equals("Admin")) {
            throw new ApiForbiddenException("You do not have permission for this request");
        }
    }



    public void saveResponseElements(ProjectEditViewDto projectResponse) {
        Project project = projectRepository.findById(projectResponse.getProject().getId()).orElseThrow();
        validateProjectExistence(project.getId());
        validateUserPermissionForProjectEdit(projectResponse.getCurrentManager().getId());
        project.setName(projectResponse.getProject().getName());
        project.setDescription(projectResponse.getProject().getDescription());
        project.setProjectManager(appUserService.findById(projectResponse.getCurrentManager().getId()));
        project.setProjectPersonnel(projectResponse.getProjectPersonnel());
        projectRepository.save(project);
    }

    public void deleteProjectById(Long projectId) {
        validateProjectExistence(projectId);
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
