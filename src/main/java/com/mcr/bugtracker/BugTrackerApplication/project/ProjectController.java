package com.mcr.bugtracker.BugTrackerApplication.project;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.project.DTO.AllProjectsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.project.DTO.ProjectDetailsViewDto;
import com.mcr.bugtracker.BugTrackerApplication.project.DTO.ProjectEditViewDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/project")
@AllArgsConstructor
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
    public ProjectEditViewDto getDataForProjectEditView(@PathVariable Long projectId) {
        return projectService.getDataForProjectEditView(projectId);
    }
    @PutMapping("/edit/{projectId}")
    public void updateProjectData(@RequestBody ProjectEditViewRequest projectResponse) {
        projectService.saveResponseElements(projectResponse);
    }
    @DeleteMapping
    public void deleteProject(@RequestBody Long projectId) {
        projectService.deleteProjectById(projectId);
    }
    @GetMapping("/details/{projectId}")
    public ProjectDetailsViewDto getDataForProjectDetailsView(@PathVariable Long projectId) {
        return projectService.getDataForProjectDetailsView(projectId);
    }
}
