package com.mcr.bugtracker.BugTrackerApplication.project;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Project {
    @SequenceGenerator(
            name = "project_sequence",
            sequenceName = "project_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "project_sequence"
    )
    private Long id;
    private String name;
    private String description;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private AppUser projectManager;
    @ManyToMany
    @JoinTable(
            name = "project_personnel",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<AppUser> projectPersonnel;
    @OneToMany(mappedBy = "project")
    @Transient
    private List<Ticket> tickets;

    public Project(AppUser projectManager) {
        this.projectManager = projectManager;
    }
}
