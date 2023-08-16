package com.mcr.bugtracker.BugTrackerApplication.project;

import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiForbiddenException;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/project")
@AllArgsConstructor
@Slf4j
public class ProjectController {
    private final ProjectService projectService;
    private final AppUserService appUserService;

    @PostMapping
    public Project createEmptyProject() {
        Project project = new Project(appUserService.getUserFromContext().orElseThrow());
        projectService.saveProject(project);
        return project;
    }
    @GetMapping
    public List<AllProjectsViewDto> getAllProjectsConnectedToUser() {
        return projectService.findAllProjectsAssignedToUser();
    }
    @GetMapping("/edit/{projectId}")
    public ProjectResponseDto getProjectDataById(@PathVariable Long projectId) {
        return projectService.getDataForProjectResponse(projectId);
    }
    @PutMapping("/edit/{projectId}")
    public void updateProjectData(@RequestBody ProjectResponseDto projectResponse, @PathVariable Long projectId) {
        projectService.saveResponseElements(projectResponse);
    }
    @DeleteMapping
    public void deleteProject(@RequestBody Long projectId) {
        projectService.deleteProjectById(projectId);
    }
    @GetMapping("/details/{projectId}")
    public ProjectDetailsViewDto getDataForProjectDetailsView(@PathVariable Long projectId) {
        ProjectDetailsViewDto projectDetailsViewDto = projectService.getDataForProjectDetailsView(projectId);
        return projectDetailsViewDto;
    }
}
