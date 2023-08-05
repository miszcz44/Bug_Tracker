package com.mcr.bugtracker.BugTrackerApplication.appuser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/user")
@AllArgsConstructor
@Slf4j
public class AppUserController {

    private final AppUserService userService;

    @DeleteMapping("delete/id")
    public String deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteUser(userId);
        return "Deleted";
    }
    @GetMapping("non-admin")
    public List<AppUser> getAllUsersExceptAdmins() {
        return userService.getAllUsersExceptAdmins();
    }

    @GetMapping
    public AppUsersResponseDto getAllUsersAndAllRoles() {
        List<AppUser> allUsers = userService.getAllUsers();
        return new AppUsersResponseDto(allUsers);
    }

    @GetMapping("/user-profile")
    public UserProfileDto getDataForUserProfile() {
        return userService.getDataForUserProfile();
    }

    @GetMapping("/{email}")
    public AppUserResponseDto getAppUserByEmail(@PathVariable String email) {
        AppUser user = userService.getUserByEmail(email);
        AppUserRole role = userService.getUserRoleByEmail(email);
        AppUserResponseDto appUser = new AppUserResponseDto(user, role);
        return appUser;
    }

    @PutMapping("change-role")
    public void changeUsersRole(@RequestBody AppUserRoleAssignmentRequest request) {
        AppUserRole assignedRole = null;
        for (AppUserRole role : AppUserRole.values()) {
            if(role.getName().equals(request.role)) {
                assignedRole = role;
                break;
            }
        }
        userService.changeUsersRole(request.usersEmails, assignedRole);
    }
    @PutMapping
    public void saveUser(@RequestBody AppUser user) {
        userService.saveUser(user);
    }
    @PutMapping("password-change")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeResponse response) {
        Boolean validationResult = userService.validatePasswordAndSetNewPassword(response.getOldPassword(), response.getNewPassword());
        if(!validationResult) {
            return ResponseEntity.ok(0);
        }
        return ResponseEntity.ok(1);
    }

}
