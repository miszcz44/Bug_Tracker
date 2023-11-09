package com.mcr.bugtracker.BugTrackerApplication.appuser;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Slf4j
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
    private String wholeName;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private AppUserRole appUserRole;
    private String sRole;
    private Boolean locked = false;
    private Boolean enabled = false;
    private Boolean demo = false;

    public AppUser(String firstName,
                   String lastName,
                   String email,
                   String password,
                   AppUserRole appUserRole,
                   String sRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.wholeName = firstName + " " + lastName;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
        this.sRole = sRole;
    }

    @OneToMany(mappedBy = "appUser",
            cascade = CascadeType.REMOVE) // REMOVE for test
    @Transient
    private Collection<ConfirmationToken> confirmationToken;

    @OneToMany(mappedBy = "assignedDeveloper")
    @JsonIgnore
    private List<Ticket> assignedTickets = new ArrayList<>();
    @OneToMany(mappedBy = "submitter")
    @JsonIgnore
    private List<Ticket> submittedTickets = new ArrayList<>();
    @OneToMany(mappedBy = "uploader")
    @JsonIgnore
    private List<Attachment> uploadedFiles = new ArrayList<>();
    @OneToMany(mappedBy = "commentator")
    @JsonIgnore
    private List<Commentary> commentaries = new ArrayList<>();
    @OneToMany(mappedBy = "projectManager")
    @JsonIgnore
    private List<Project> managedProjects = new ArrayList<>();
    @ManyToMany(mappedBy = "projectPersonnel")
    @JsonIgnore
    private List<Project> assignedProjects = new ArrayList<>();

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

    public Boolean isDemo() {
        return demo;
    }

    public Collection<ConfirmationToken> getConfirmationToken() {
        return confirmationToken;
    }

    public void setConfirmationToken(Collection<ConfirmationToken> confirmationToken) {
        this.confirmationToken = confirmationToken;
    }

    public static final class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String wholeName;
        private String email;
        private String password;
        private String sRole;
        private Boolean locked = false;
        private Boolean enabled = false;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder wholeName(String wholeName) {
            this.wholeName = wholeName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder sRole(String sRole) {
            this.sRole = sRole;
            return this;
        }

        public Builder locked() {
            this.locked = true;
            return this;
        }

        public Builder enabled() {
            this.enabled = true;
            return this;
        }

        public AppUser build() {
            AppUser appUser = new AppUser();
            appUser.id = this.id;
            appUser.firstName = this.firstName;
            appUser.lastName = this.lastName;
            appUser.wholeName = this.wholeName;
            appUser.sRole = this.sRole;
            appUser.email = this.email;
            appUser.password = this.password;
            appUser.locked = this.locked;
            appUser.enabled = this.enabled;
            return appUser;
        }
    }

}
