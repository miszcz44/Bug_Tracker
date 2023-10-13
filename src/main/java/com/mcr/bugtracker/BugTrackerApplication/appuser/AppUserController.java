package com.mcr.bugtracker.BugTrackerApplication.appuser;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping(path = "api/v1/user")
@AllArgsConstructor
public class AppUserController {

    private final AppUserService userService;

    @GetMapping("/role-management")
    public AppUsersResponseDto getAllUsersAndAllRoles() {
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

// prolly not necessary    @GetMapping("/{email}")
//    public AppUserResponseDto getAppUserByEmail(@PathVariable String email) {
//        AppUser user = userService.getUserByEmail(email);
//        AppUserRole role = userService.getUserRoleByEmail(email);
//        AppUserResponseDto appUser = new AppUserResponseDto(user, role);
//        return appUser;
//    }

    @PutMapping("/role-management/change-role")
    public void changeUsersRole(@RequestBody AppUserRoleAssignmentRequest request) {
        userService.changeUsersRole(request);
    }
//   prolly not necessary @PutMapping
//    public void saveUser(@RequestBody AppUser user) {
//        userService.saveUser(user);
//    }
    @PutMapping("password-change")
    public void changePassword(@RequestBody PasswordChangeRequest request) {
        userService.validatePasswordMatchAndSetNewPassword(request.getOldPassword(), request.getNewPassword());
    }

    @PutMapping("email-change")
    public int changeEmail(@RequestBody EmailChangeRequest request) {
        return userService.validateEmailChange(request.getNewEmail(), request.getPassword());
    }

    @GetMapping("/dashboard")
    public DashboardViewDto getDataForDashboardView() {
        return userService.getDataForDashboardView();
    }

}
