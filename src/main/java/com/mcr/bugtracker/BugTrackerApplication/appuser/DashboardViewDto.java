package com.mcr.bugtracker.BugTrackerApplication.appuser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class DashboardViewDto {
    private Long projectId;
    private String projectName;
    private String projectDescription;
    private Long ticketId;
    private String ticketTitle;
    private String ticketDescription;
}
