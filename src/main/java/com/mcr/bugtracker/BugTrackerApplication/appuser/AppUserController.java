package com.mcr.bugtracker.BugTrackerApplication.appuser;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(path = "api/v1/user")
@AllArgsConstructor
public class AppUserController {
    private final AppUserService userService;
    @GetMapping("/role-management")
    public RoleManagementDto getAllUsersAndAllRoles() {
        return userService.getDataForRoleManagement();
    }
    @GetMapping("/roles")
    public AppUserRole[] getRoles() {
        return userService.getNonAdminAndNonDemoRoles();
    }
    @GetMapping("/user-profile")
    public UserProfileDto getDataForUserProfile() {
        return userService.getDataForUserProfile();
    }
    @PutMapping("/role-management/change-role")
    public void changeUsersRole(@RequestBody AppUserRoleAssignmentRequest request) {
        userService.changeUsersRole(request);
    }
    @PutMapping("password-change")
    public void changePassword(@RequestBody PasswordChangeRequest request) {
        userService.validatePasswordMatchAndSetNewPassword(request.getOldPassword(), request.getNewPassword());
    }
    @PutMapping("email-change")
    public void changeEmail(@RequestBody EmailChangeRequest request) {
        userService.validateEmailChangeAndSetNewEmail(request.getNewEmail(), request.getPassword());
    }
    @GetMapping("/dashboard")
    public DashboardViewDto getDataForDashboardView() {
        return userService.getDataForDashboardView();
    }
}
