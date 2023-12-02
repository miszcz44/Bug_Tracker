package com.mcr.bugtracker.BugTrackerApplication.appuser;

import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiEmailTakenException;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiNotFoundException;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiPasswordDoesntMatchException;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiRequestException;
import com.mcr.bugtracker.BugTrackerApplication.appuser.DTO.AppUserDto;
import com.mcr.bugtracker.BugTrackerApplication.appuser.DTO.DashboardViewDto;
import com.mcr.bugtracker.BugTrackerApplication.appuser.DTO.RoleManagementDto;
import com.mcr.bugtracker.BugTrackerApplication.appuser.DTO.UserProfileDto;
import com.mcr.bugtracker.BugTrackerApplication.appuser.Mapper.AppUserDtoMapper;
import com.mcr.bugtracker.BugTrackerApplication.appuser.Mapper.UserProfileDtoMapper;
import com.mcr.bugtracker.BugTrackerApplication.appuser.Request.AppUserRoleAssignmentRequest;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.registration.token.ConfirmationToken;
import com.mcr.bugtracker.BugTrackerApplication.registration.token.ConfirmationTokenService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Getter
public class AppUserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";
    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserProfileDtoMapper userProfileDtoMapper;
    private final AppUserDtoMapper appUserDtoMapper;
    private static final Random RANDOM = new Random();
    @Override
    public AppUser loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    public void checkIfEmailTakenOrNotConfirmed(AppUser appUser) {
        if(appUserRepository.findByEmail(appUser.getEmail()).isPresent()) {
            AppUser user = appUserRepository.findByEmail(appUser.getEmail()).get();
            if(!user.getEnabled()) {
                throw new ApiRequestException("Confirm email");
            }
            throw new ApiRequestException("Email already taken");
        }
    }
    public String generateAndSaveConfirmationTokenForGivenUser(AppUser appUser) {
        String token = UUID.randomUUID().toString();
        confirmationTokenService.saveConfirmationToken(new ConfirmationToken(
                                                        token,
                                                        LocalDateTime.now(),
                                                        LocalDateTime.now().plusMinutes(15),
                                                        appUser));
        return token;
    }
    public void signUpUser(AppUser appUser) {
        checkIfEmailTakenOrNotConfirmed(appUser);
        appUser.setPassword(bCryptPasswordEncoder
                .encode(appUser.getPassword()));
        appUser.setEnabled(true);
        appUserRepository.save(appUser);
    }
    public void enableAppUser(AppUser appUser) {
        appUser.setEnabled(true);
        appUserRepository.save(appUser);
    }
    public Optional<AppUser> getUserFromContext() {
        AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUserRepository.findById(user.getId());
    }
    public void changeUsersRole(AppUserRoleAssignmentRequest request) {
        AppUserRole role = findRoleObject(request.getRole());
        for(String email : request.getUsersEmails()) {
            AppUser user = appUserRepository.findByEmail(email).orElseThrow();
            user.setAppUserRole(role);
            user.setSRole(request.getRole());
            appUserRepository.save(user);
        }
    }
    public AppUserRole findRoleObject(String roleName) {
        AppUserRole assignedRole = null;
        for(AppUserRole role : AppUserRole.values()) {
            if(role.getName().equals(roleName)) {
                assignedRole = role;
            }
        }
        if(assignedRole == null) {
            throw new NoSuchElementException();
        }
        return assignedRole;
    }
    public void validatePasswordMatchAndSetNewPassword(String oldPassword, String newPassword) {
        AppUser user = getUserFromContext().orElseThrow();
        if(bCryptPasswordEncoder.matches(oldPassword.subSequence(0, oldPassword.length()), user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword.subSequence(0, newPassword.length())));
            appUserRepository.save(user);
        }
        else {
            throw new ApiPasswordDoesntMatchException("Password does not match");
        }
    }

    public AppUser findById(Long id) {
        return appUserRepository.findById(id).orElseThrow();
    }
    public UserProfileDto getDataForUserProfile() {
        AppUser user = getUserFromContext().orElseThrow();
        return userProfileDtoMapper.apply(user);
    }
    public void validateEmailChangeAndSetNewEmail(String newEmail, String password) {
        if(isEmailTaken(newEmail)) {
            throw new ApiEmailTakenException("Email already taken");
        }
        if(!isPasswordCorrect(password)){
            throw new ApiPasswordDoesntMatchException("Password doesn't match");
        }
        changeEmail(newEmail);
    }
    protected void changeEmail(String newEmail) {
        AppUser user = getUserFromContext().orElseThrow();
        user.setEmail(newEmail);
        appUserRepository.save(user);
    }
    protected boolean isPasswordCorrect(String password) {
        AppUser user = getUserFromContext().orElseThrow();
        return bCryptPasswordEncoder.matches(password.subSequence(0, password.length()), user.getPassword());
    }
    protected boolean isEmailTaken(String newEmail) {
        return appUserRepository.existsByEmail(newEmail);
    }
    public DashboardViewDto getDataForDashboardView() {
        AppUser currentUser = getUserFromContext().orElseThrow();
        switch (currentUser.getSRole()) {
            case "Admin":
                return null;
            case "Project manager":
                return getDataForProjectManagerDashboardView(currentUser);
            case "Developer":
                return getDataForDeveloperDashboardView(currentUser);
            case "Submitter":
                return getDataForSubmitterDashboardView(currentUser);
            case "None":
                return getDataForNoRoleDashboardView(currentUser);
        }
        throw new ApiNotFoundException("Resource not found");
    }
    protected DashboardViewDto getDataForNoRoleDashboardView(AppUser currentUser) {
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        List<Project> belongingProjects = currentUser.getAssignedProjects();
        setValuesFromRandomProject(dashboardViewDto, belongingProjects);
        return dashboardViewDto;
    }
    protected DashboardViewDto getDataForSubmitterDashboardView(AppUser currentUser) {
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        List<Project> belongingProjects = currentUser.getAssignedProjects();
        setValuesFromRandomProject(dashboardViewDto, belongingProjects);
        List<Ticket> submittedTickets = currentUser.getSubmittedTickets();
        setValuesFromRandomTicket(dashboardViewDto, submittedTickets);
        return dashboardViewDto;
    }
    protected DashboardViewDto getDataForDeveloperDashboardView(AppUser currentUser) {
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        List<Project> belongingProjects = currentUser.getAssignedProjects();
        setValuesFromRandomProject(dashboardViewDto, belongingProjects);
        List<Ticket> assignedTickets = currentUser.getAssignedTickets();
        setValuesFromRandomTicket(dashboardViewDto, assignedTickets);
        return dashboardViewDto;
    }
    protected DashboardViewDto getDataForProjectManagerDashboardView(AppUser currentUser) {
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        List<Project> allProjects = Stream.concat(currentUser.getManagedProjects().stream(),
                currentUser.getAssignedProjects().stream()).collect(Collectors.toList());
        setValuesFromRandomProject(dashboardViewDto, allProjects);
        List<Ticket> submittedTickets = currentUser.getSubmittedTickets();
        if (submittedTickets.size() > 0) {
            setValuesFromRandomTicket(dashboardViewDto, submittedTickets);
        }
        else {
            Collections.shuffle(allProjects);
            for(Project project : allProjects) {
                List<Ticket> ticketsInProject = project.getTickets();
                setValuesFromRandomTicket(dashboardViewDto, ticketsInProject);
                if(ticketsInProject.size() > 0) break;
            }
        }
        return dashboardViewDto;
    }
    protected void setValuesFromRandomTicket(DashboardViewDto dashboardViewDto, List<Ticket> tickets) {
        if(tickets.size() > 0) {
            Ticket ticket = tickets.get(RANDOM.nextInt(tickets.size()));
            dashboardViewDto.setTicketId(ticket.getId());
            dashboardViewDto.setTicketTitle(ticket.getOptionalTitle().orElse("Title not specified"));
            dashboardViewDto.setTicketDescription(ticket.getOptionalDescription().orElse("Description not specified"));
        }
    }
    protected void setValuesFromRandomProject(DashboardViewDto dashboardViewDto, List<Project> projects) {
        if(projects.size() > 0) {
            Project project = projects.get(RANDOM.nextInt(projects.size()));
            dashboardViewDto.setProjectId(project.getId());
            dashboardViewDto.setProjectName(project.getOptionalName().orElse("Name not specified"));
            dashboardViewDto.setProjectDescription(project.getOptionalDescription()
                    .orElse("Description not specified"));
        }
    }
    public AppUserRole[] getNonAdminAndNonDemoRoles() {
        return new AppUserRole[]{
                AppUserRole.PROJECT_MANAGER, AppUserRole.DEVELOPER, AppUserRole.SUBMITTER, AppUserRole.NONE};
    }
    public RoleManagementDto getDataForRoleManagement() {
        AppUser appUser = getUserFromContext().orElseThrow();
        List<AppUser> allUsers = appUserRepository.findAll();
        if(appUser.isDemo()) {
            return getDataForRoleManagement(allUsers.stream()
                    .filter(user -> user.isDemo() != null & user.isDemo())
                    .collect(Collectors.toList()));
        }
        else {
            return getDataForRoleManagement(allUsers);
        }
    }
    protected RoleManagementDto getDataForRoleManagement(List<AppUser> allUsers) {
        return new RoleManagementDto(
                allUsers.stream()
                        .map(appUserDtoMapper)
                        .collect(Collectors.toList()),
                allUsers.stream()
                        .filter(user -> !("Admin").equals(user.getSRole()))
                        .map(AppUser::getEmail)
                        .collect(Collectors.toList()),
                getNonAdminAndNonDemoRoles()
        );
    }

    public List<AppUserDto> getAllUsersNotInProject(Project project) {
        List<AppUser> appUsers = appUserRepository.findAll().stream()
                .filter(user -> !project.getProjectPersonnel().contains(user))
                .filter(user -> !user.equals(project.getProjectManager()))
                .filter(user -> !"Admin".equals(user.getSRole()))
                .collect(Collectors.toList());
        if(project.getProjectManager().isDemo()) {
            return appUsers.stream()
                    .filter(AppUser::isDemo)
                    .map(appUserDtoMapper)
                    .collect(Collectors.toList());
        }
        else {
            return appUsers.stream()
                    .map(appUserDtoMapper)
                    .collect(Collectors.toList());
        }
    }
}
