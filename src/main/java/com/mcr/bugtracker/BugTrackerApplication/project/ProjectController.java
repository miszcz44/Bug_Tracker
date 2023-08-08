package com.mcr.bugtracker.BugTrackerApplication.project;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<?> getAllProjectsConnectedToUser() {
        return ResponseEntity.ok(projectService.findAllProjectsAssignedToUser());
    }

    @GetMapping("{projectId}")
    public ProjectResponseDto getProjectDataById(@PathVariable Long projectId) {
        ProjectResponseDto projectResponseDto = projectService.getDataForProjectResponse(projectId);
        return projectResponseDto;
    }
    @PostMapping("/add-users")
    public void addUsers(@RequestBody UsersToProjectRequest request) {
        projectService.addUsersToProject(request);
    }

    @PutMapping("{projectId}")
    public ResponseEntity<?> updateTicketData(@RequestBody ProjectResponseDto projectResponse, @PathVariable Long projectId) {
        projectService.saveResponseElements(projectResponse);
        return ResponseEntity.ok("updatedProject");
    }

    @PutMapping("{projectId}/add-user-to-project")
    public ResponseEntity<?> addUserToProject(@RequestBody List<String> emails, @PathVariable Long projectId) {
        Project project = projectService.findById(projectId).orElseThrow();
        for (String email: emails) {
            projectService.addUserToProjectByEmail(project, email);
        }
        projectService.saveProject(project);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping
    public void deleteProject(@RequestBody Long projectId) {
        projectService.deleteProjectById(projectId);
    }

    @DeleteMapping("{projectId}/delete-user-from-project")
    public ResponseEntity<?> deleteUserFromProject(@RequestBody Long userId, @PathVariable Long projectId) {
        projectService.deleteUserFromProject(userId);
        return ResponseEntity.ok("deleted");
    }

    @GetMapping("/details/{projectId}")
    public ProjectDetailsViewDto getDataForProjectDetailsView(@PathVariable Long projectId) {
        ProjectDetailsViewDto projectDetailsViewDto = projectService.getDataForProjectDetailsView(projectId);
        return projectDetailsViewDto;
    }
}
