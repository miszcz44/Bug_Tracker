package com.mcr.bugtracker.BugTrackerApplication.appuser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.registration.token.ConfirmationToken;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import com.mcr.bugtracker.BugTrackerApplication.ticket.attachment.Attachment;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.Commentary;
import com.mcr.bugtracker.BugTrackerApplication.util.GrantedAuthorityDeserializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class AppUser implements UserDetails {


    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;
    private Boolean locked = false;
    private Boolean enabled = false;

    public AppUser(String firstName,
                   String lastName,
                   String email,
                   String password,
                   AppUserRole appUserRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
    }

    @OneToMany(mappedBy = "appUser",
            cascade = CascadeType.REMOVE) // REMOVE for test
    @Transient
    private Collection<ConfirmationToken> confirmationToken;

    @OneToMany(mappedBy = "assignedDeveloper")
    @Transient
    private List<Ticket> assignedTicket;
    @OneToMany(mappedBy = "submitter")
    @Transient
    private List<Ticket> submittedTicket;
    @OneToMany(mappedBy = "uploader")
    private List<Attachment> uploadedFiles;
    @OneToMany(mappedBy = "commentator")
    private List<Commentary> commentaries;
    @OneToMany(mappedBy = "projectManager")
    @Transient
    private List<Project> managedProject;
    @ManyToMany(mappedBy = "projectPersonnel")
    @Transient
    private List<Project> assignedProjects;

    @Override
    @JsonIgnore
    @JsonDeserialize(using = GrantedAuthorityDeserializer.class)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }



    public Collection<ConfirmationToken> getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(Collection<ConfirmationToken> confirmationToken) {
        this.confirmationToken = confirmationToken;
    }
}
