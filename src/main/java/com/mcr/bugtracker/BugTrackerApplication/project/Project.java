package com.mcr.bugtracker.BugTrackerApplication.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private List<Ticket> tickets;

    public Project(AppUser projectManager) {
        this.projectManager = projectManager;
    }

    public static final class Builder {
        private Long id;
        private String name;
        private String description;
        private AppUser projectManager;
        private List<AppUser> projectPersonnel;
        private List<Ticket> tickets;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder projectManager(AppUser projectManager) {
            this.projectManager = projectManager;
            return this;
        }

        public Builder projectPersonnel(List<AppUser> projectPersonnel) {
            this.projectPersonnel = projectPersonnel;
            return this;
        }

        public Builder tickets(List<Ticket> tickets) {
            this.tickets = tickets;
            return this;
        }

        public Project build() {
            Project project = new Project();
            project.description = this.description;
            project.id = this.id;
            project.projectManager = this.projectManager;
            project.projectPersonnel = this.projectPersonnel;
            project.tickets = this.tickets;
            return project;
        }
    }
}
