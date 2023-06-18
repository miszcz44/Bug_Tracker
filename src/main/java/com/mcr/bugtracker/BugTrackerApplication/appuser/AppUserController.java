package com.mcr.bugtracker.BugTrackerApplication.appuser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public AppUserResponseDto getAllUsersAndAllRoles() {
        List<AppUser> allUsers = userService.getAllUsers();
        return new AppUserResponseDto(allUsers);
    }

    @PutMapping("change-role")
    public void changeUserRole(@RequestBody String smth) {
        log.info(smth);
    }
}
