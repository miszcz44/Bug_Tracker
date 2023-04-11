package com.mcr.bugtracker.BugTrackerApplication.project;

import lombok.AllArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/project")
@AllArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public void saveProject(@RequestBody ProjectRequest request) {
        projectService.saveProject(request);
    }

    @PostMapping("/add-users")
    public void addUsers(@RequestBody UsersToProjectRequest request) {
        projectService.addUsersToProject(request);
    }
}
