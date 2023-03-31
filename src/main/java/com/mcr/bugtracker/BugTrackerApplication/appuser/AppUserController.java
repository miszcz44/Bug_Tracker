package com.mcr.bugtracker.BugTrackerApplication.appuser;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/user")
@AllArgsConstructor
public class AppUserController {

    private final AppUserService userService;

    @DeleteMapping("delete/id")
    public String deleteUser(@RequestParam("userId") Long userId) {
        userService.deleteUser(userId);
        return "Deleted";
    }
}
