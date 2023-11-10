package com.mcr.bugtracker.BugTrackerApplication.project;

import java.util.List;

import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiForbiddenException;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiNotFoundException;
import com.mcr.bugtracker.BugTrackerApplication.appuser.*;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketForProjectViewDtoMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final AppUserService appUserService;
    private final AllProjectsViewMapper allProjectsViewMapper;
    private final AppUserDtoMapper appUserDtoMapper;
    private final ProjectDtoMapper projectDtoMapper;
    private final TicketForProjectViewDtoMapper ticketForProjectViewDtoMapper;

    public void saveProject(Project project) {
        projectRepository.save(project);
    }
    public List<AllProjectsViewDto> findAllProjectsAssignedToUser() {
        AppUser user = appUserService.getUserFromContext().orElseThrow();
        List<Project> allProjectsViewDtos = projectRepository.findAll();
        if("Admin".equals(user.getSRole()) && user.isDemo()) {
            return allProjectsViewDtos.stream()
                    .filter(project -> project.getProjectManager().isDemo())
                    .map(allProjectsViewMapper)
                    .collect(Collectors.toList());
        }
        else if("Admin".equals(user.getSRole())) {
            return allProjectsViewDtos.stream()
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
    public ProjectDetailsViewDto getDataForProjectDetailsView(Long projectId) {
        validateProjectExistence(projectId);
        Project project = projectRepository.findById(projectId).orElseThrow();
        validateUserPermissionForProjectDetails(project);
        return new ProjectDetailsViewDto(
                projectDtoMapper.apply(project),
                project.getProjectManager().getWholeName(),
                project.getProjectManager().getEmail(),
                project.getProjectPersonnel().stream()
                        .map(appUserDtoMapper)
                        .collect(Collectors.toList()),
                project.getTickets().stream()
                        .map(ticketForProjectViewDtoMapper)
                        .collect(Collectors.toList()));
    }
    public ProjectEditViewDto getDataForProjectEditView(Long projectId) {
        validateProjectExistence(projectId);
        Project project = projectRepository.findById(projectId).orElseThrow();
        validateUserPermissionForProjectEdit(project.getProjectManager().getId());
        return new ProjectEditViewDto(projectDtoMapper.apply(project),
                appUserDtoMapper.apply(project.getProjectManager()),
                project.getProjectPersonnel().stream()
                        .filter(user -> "Project manager".equals(user.getSRole()))
                        .map(appUserDtoMapper)
                        .collect(Collectors.toList()),
                project.getProjectPersonnel().stream()
                        .map(appUserDtoMapper)
                        .collect(Collectors.toList()),
                appUserService.getAllUsersNotInProject(project));
    }
    protected void validateUserPermissionForProjectEdit(Long managerId) {
        AppUser currentUser = appUserService.getUserFromContext().orElseThrow();
        if (!currentUser.getId().equals(managerId) && !currentUser.getSRole().equals("Admin")) {
            throw new ApiForbiddenException("You do not have permission for this request");
        }
    }
    protected void validateProjectExistence(Long projectId) {
        if(projectRepository.findById(projectId).isEmpty()) {
            throw new ApiNotFoundException("There is no such resource");
        }
    }
    protected void validateUserPermissionForProjectDetails(Project project) {
        AppUser currentUser = appUserService.getUserFromContext().orElseThrow();
        if(!project.getProjectPersonnel().contains(currentUser) && !currentUser.equals(project.getProjectManager()) &&
            !currentUser.getSRole().equals("Admin")) {
            throw new ApiForbiddenException("You do not have permission for this request");
        }
    }
    protected void validateUserPermissionForProjectDelete(Project project) {
        AppUser currentUser = appUserService.getUserFromContext().orElseThrow();
        if(!currentUser.equals(project.getProjectManager()) && !currentUser.getSRole().equals("Admin")) {
            throw new ApiForbiddenException("You do not have permission for this request");
        }
    }
    public void saveResponseElements(ProjectEditViewResponse projectResponse) {
        Project project = projectResponse.getProject();
        validateProjectExistence(project.getId());
        validateUserPermissionForProjectEdit(projectResponse.getCurrentManager().getId());
        project.setProjectPersonnel(projectResponse.getProjectPersonnel());
        project.setProjectManager(projectResponse.getCurrentManager());
        projectRepository.save(project);
    }
    public void deleteProjectById(Long projectId) {
        validateProjectExistence(projectId);
        Project project = projectRepository.findById(projectId).orElseThrow();
        validateUserPermissionForProjectDelete(project);
        projectRepository.deleteById(projectId);
    }
}