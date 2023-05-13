package com.mcr.bugtracker.BugTrackerApplication.project;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<?> createEmptyProject() {
        Project project = new Project(appUserService.getUserFromContext().orElseThrow());
        projectService.saveProject(project);
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<?> getAllProjectsConnectedToUser() {
        return ResponseEntity.ok(projectService.findAllProjectsAssignedToUser());
    }

    @GetMapping("{projectId}")
    public ResponseEntity<?> getProjectDataById(@PathVariable Long projectId) {
        Optional<Project> projectOpt = projectService.findById(projectId);
        String name = appUserService.getNameOfTheLoggedUser();
        List<AppUser> projectPersonnel = projectService.getProjectPersonnel(projectOpt.orElseThrow().getId());
        List<AppUser> allUsers = appUserService.getAllUsersExceptTheLoggedOneAndProjectPersonnel(projectPersonnel);
        return ResponseEntity.ok(new ProjectResponseDto(projectOpt.orElse(new Project()), name, projectPersonnel, allUsers));
    }
    @PostMapping("/add-users")
    public void addUsers(@RequestBody UsersToProjectRequest request) {
        projectService.addUsersToProject(request);
    }

    @PutMapping("{projectId}")
    public ResponseEntity<?> updateTicketData(@RequestBody Project project, @PathVariable Long projectId) {
        Project updatedProject = projectService.saveProject(project);
        return ResponseEntity.ok(updatedProject);
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

    @DeleteMapping("{projectId}/delete-user-from-project")
    public ResponseEntity<?> deleteUserFromProject(@RequestBody Long userId, @PathVariable Long projectId) {
        projectService.deleteUserFromProject(userId);
        return ResponseEntity.ok("deleted");
    }
}
