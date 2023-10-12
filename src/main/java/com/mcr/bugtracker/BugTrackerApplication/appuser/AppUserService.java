package com.mcr.bugtracker.BugTrackerApplication.appuser;

import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiRequestException;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.registration.token.ConfirmationToken;
import com.mcr.bugtracker.BugTrackerApplication.registration.token.ConfirmationTokenService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserProfileDtoMapper userProfileDtoMapper;
    private static final Random RANDOM = new Random();

    @Override
    public AppUser loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public void checkIfEmailTakenOrNotConfirmed(AppUser appUser) {
        Optional<AppUser> potentialUser = appUserRepository
                .findByEmail(appUser.getEmail());

        if (!potentialUser.equals(Optional.empty())) {
            AppUser existingUser = potentialUser.get();
            if(!existingUser.getEnabled()) {
                throw new ApiRequestException("Confirm email");
            }
            throw new ApiRequestException("email already taken");
        }
    }

    public String generateAndSaveConfirmationTokenForGivenUser(AppUser appUser) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(
                confirmationToken);
        return token;
    }
    public void signUpUser(AppUser appUser) {
        checkIfEmailTakenOrNotConfirmed(appUser);

        String encodedPassword = bCryptPasswordEncoder
                .encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public void deleteUser(Long userId) {
        if(!appUserRepository.existsById(userId)) {
            throw new IllegalStateException("User of this id does not exist");
        }
        appUserRepository.deleteById(userId);
    }

    public Optional<AppUser> getUserFromContext() {
        AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUserRepository.findById(user.getId());
    }

    public String getNameOfTheLoggedUser() {
        AppUser user = getUserFromContext().orElseThrow();
        return user.getFirstName() + " " + user.getLastName();
    }

    public List<AppUser> getAllUsersExceptTheLoggedOneAndProjectPersonnel(List<AppUser> projectPersonnel) {
        List<Long> idList = projectPersonnel.stream()
                .map(AppUser::getId)
                .collect(Collectors.toList());
        Long projectManagerId = getUserFromContext().orElseThrow().getId();
        log.info(projectManagerId.toString());
        if(idList.isEmpty()) {
            return appUserRepository.getAllUsersButAdminsAndProjectManager(projectManagerId);
        }
        return appUserRepository.getAllUsersButAdminsProjectManagerAndPersonnel(projectManagerId, idList);
    }

    public List<AppUser> getAllNonAdminUsersNotParticipatingInProject(List<AppUser> projectPersonnel, AppUser manager) {
        List<Long> idList = projectPersonnel.stream()
                .map(AppUser::getId)
                .collect(Collectors.toList());
        if(idList.isEmpty()) {
            return appUserRepository.getAllUsersButAdminsAndProjectManager(manager.getId());
        }
        return appUserRepository.getAllUsersButAdminsProjectManagerAndPersonnel(manager.getId(), idList);
    }

    public List<AppUser> getAllNonAdminDemoUsersNotParticipatingInProject(List<AppUser> projectPersonnel, AppUser manager) {
        List<Long> idList = projectPersonnel.stream()
                .map(AppUser::getId)
                .collect(Collectors.toList());
        if(idList.isEmpty()) {
            return appUserRepository.getAllUsersButAdminsAndProjectManager(manager.getId())
                    .stream()
                    .filter(appUser -> appUser.isDemo())
                    .collect(Collectors.toList());
        }
        return appUserRepository.getAllUsersButAdminsProjectManagerAndPersonnel(manager.getId(), idList)
                .stream()
                .filter(appUser -> appUser.isDemo())
                .collect(Collectors.toList());
    }


    public List<AppUser> findAllUsersAssignedToProject(Long id) {
        return appUserRepository.getProjectPersonnel(id);
    }

    public List<AppUser> getAllUsersExceptAdmins() {
        return appUserRepository.getAllUsersExceptAdmins();
    }

    public void changeUsersRole(AppUserRoleAssignmentRequest request) {
        AppUserRole role = findRoleObject(request.getRole());
        for(String email : request.getUsersEmails()) {
            AppUser user = appUserRepository.findByEmail(email).get();
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
        return assignedRole;
    }

    public String getRoleByEmail(String email) {
        return appUserRepository.findRoleByEmail(email).getName();
    }

    public AppUser getUserByEmail(String email) {
        return appUserRepository.findByEmail(email).get();
    }

    public void saveUser(AppUser user) {
        appUserRepository.save(user);
    }

    public AppUserRole getUserRoleByEmail(String email) {
        return appUserRepository.findRoleByEmail(email);
    }

    public Boolean validatePasswordAndSetNewPassword(String oldPassword, String newPassword) {
        AppUser user = getUserFromContext().get();
        log.info(user.getPassword());
        if(bCryptPasswordEncoder.matches(oldPassword.subSequence(0, oldPassword.length()), user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(newPassword.subSequence(0, newPassword.length())));
            appUserRepository.save(user);
            return true;
        }
        return false;
    }

    public List<AppUser> getDemandedPersonnelDataForProjectView(List<AppUser> projectPersonnel) {
        List<AppUser> personnelWithDemandedData = new ArrayList<>();
        for(AppUser user : projectPersonnel) {
            personnelWithDemandedData.add(new AppUser.Builder().
                    wholeName(user.getWholeName()).
                    email(user.getEmail()).
                    sRole(user.getSRole()).build());
        }
        return personnelWithDemandedData;
    }

    public List<AppUser> findProjectManagersParticipatingInProject(List<AppUser> projectPersonnel) {
        List<AppUser> projectManagers = new ArrayList<>();
        for(AppUser appUser : projectPersonnel) {
            if(appUser.getSRole().equals("Project manager")) {
                AppUser projectManagerWithDemandedData = new AppUser.Builder()
                        .id(appUser.getId())
                        .email(appUser.getEmail())
                        .wholeName(appUser.getWholeName())
                        .sRole(appUser.getSRole())
                        .build();
                projectManagers.add(projectManagerWithDemandedData);
            }
        }
        return projectManagers;
    }

    public AppUser findById(Long id) {
        return appUserRepository.findById(id).orElseThrow();
    }

    public AppUser getDeveloperForTicketEditView(AppUser assignedDeveloper) {
        return new AppUser.Builder()
                .id(assignedDeveloper.getId())
                .wholeName(assignedDeveloper.getWholeName())
                .email(assignedDeveloper.getEmail())
                .build();
    }
    public List<AppUser> getProjectDevelopers(Project project) {
        List<AppUser> developersWithDemandedData = new ArrayList<>();
        for (AppUser user : project.getProjectPersonnel()) {
            if(user.getSRole().equals("Developer")) {
                developersWithDemandedData.add(new AppUser.Builder()
                        .id(user.getId())
                        .wholeName(user.getWholeName())
                        .email(user.getEmail())
                        .build());
            }
        }
        return developersWithDemandedData;
    }

    public UserProfileDto getDataForUserProfile() {
        AppUser user = getUserFromContext().orElseThrow();
        return userProfileDtoMapper.apply(user);
    }

    public int validateEmailChange(String newEmail, String password) {
        if(isEmailTaken(newEmail)) {
            return 0;
        }
        if(!isPasswordCorrect(password)){
            return -1;
        }
        changeEmail(newEmail);
        return 1;
    }

    private void changeEmail(String newEmail) {
        AppUser user = getUserFromContext().get();
        user.setEmail(newEmail);
        appUserRepository.save(user);
    }

    private boolean isPasswordCorrect(String password) {
        AppUser user = getUserFromContext().get();
        if(!bCryptPasswordEncoder.matches(password.subSequence(0, password.length()), user.getPassword())) {
            return false;
        }
        return true;
    }

    private boolean isEmailTaken(String newEmail) {
        if(!appUserRepository.existsByEmail(newEmail)) {
            return false;
        }
        return true;
    }

    public DashboardViewDto getDataForDashboardView() {
        AppUser currentUser = getUserFromContext().get();
        if(currentUser.getSRole().equals("Project manager")) {
            return getDataForProjectManagerDashboardView(currentUser);
        }
        else if(currentUser.getSRole().equals("Developer")) {
            return getDataForDeveloperDashboardView(currentUser);
        }
        else if(currentUser.getSRole().equals("Submitter")) {
            return getDataForSubmitterDashboardView(currentUser);
        }
        else if(currentUser.getSRole().equals("None")) {
            return getDataForNoRoleDashboardView(currentUser);
        }
        return null;
    }

    private DashboardViewDto getDataForNoRoleDashboardView(AppUser currentUser) {
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        List<Project> belongingProjects = currentUser.getAssignedProjects();
        dashboardViewDto = setValuesFromRandomProject(dashboardViewDto, belongingProjects);
        return dashboardViewDto;
    }

    private DashboardViewDto getDataForSubmitterDashboardView(AppUser currentUser) {
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        List<Project> belongingProjects = currentUser.getAssignedProjects();
        dashboardViewDto = setValuesFromRandomProject(dashboardViewDto, belongingProjects);
        List<Ticket> submittedTickets = currentUser.getSubmittedTickets();
        dashboardViewDto = setValuesFromRandomTicket(dashboardViewDto, submittedTickets);
        return dashboardViewDto;
    }

    private DashboardViewDto getDataForDeveloperDashboardView(AppUser currentUser) {
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        List<Project> belongingProjects = currentUser.getAssignedProjects();
        dashboardViewDto = setValuesFromRandomProject(dashboardViewDto, belongingProjects);
        List<Ticket> assignedTickets = currentUser.getAssignedTickets();
        dashboardViewDto = setValuesFromRandomTicket(dashboardViewDto, assignedTickets);
        return dashboardViewDto;
    }

    private DashboardViewDto getDataForProjectManagerDashboardView(AppUser currentUser) {
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        List<Project> allProjects = Stream.concat(currentUser.getManagedProjects().stream(),
                currentUser.getAssignedProjects().stream()).collect(Collectors.toList());
        dashboardViewDto = setValuesFromRandomProject(dashboardViewDto, allProjects);
        List<Ticket> submittedTickets = currentUser.getSubmittedTickets();
        if (submittedTickets.size() > 0) {
            dashboardViewDto = setValuesFromRandomTicket(dashboardViewDto, submittedTickets);
        }
        else {
            Collections.shuffle(allProjects);
            for(Project project : allProjects) {
                List<Ticket> ticketsInProject = project.getTickets();
                if(ticketsInProject.size() > 0) {
                    Ticket ticket = ticketsInProject.get(RANDOM.nextInt(ticketsInProject.size()));
                    dashboardViewDto.setTicketId(ticket.getId());
                    dashboardViewDto.setTicketTitle(ticket.getTitle());
                    dashboardViewDto.setTicketDescription(ticket.getDescription());
                    break;
                }
            }
        }
        return dashboardViewDto;
    }
    private DashboardViewDto setValuesFromRandomTicket(DashboardViewDto dashboardViewDto, List<Ticket> tickets) {
        if(tickets.size() > 0) {
            Ticket ticket = tickets.get(RANDOM.nextInt(tickets.size()));
            dashboardViewDto.setTicketId(ticket.getId());
            dashboardViewDto.setTicketTitle(ticket.getTitle());
            dashboardViewDto.setTicketDescription(ticket.getDescription());
            return dashboardViewDto;
        }
        return dashboardViewDto;
    }

    private DashboardViewDto setValuesFromRandomProject(DashboardViewDto dashboardViewDto, List<Project> projects) {
        if(projects.size() > 0) {
            Project project = projects.get(RANDOM.nextInt(projects.size()));
            dashboardViewDto.setProjectId(project.getId());
            dashboardViewDto.setProjectTitle(project.getName());
            dashboardViewDto.setProjectDescription(project.getDescription());
            return dashboardViewDto;
        }
        return dashboardViewDto;
    }

    public AppUserRole[] getNonAdminAndNonDemoRoles() {
        AppUserRole[] userRoles = {
                AppUserRole.PROJECT_MANAGER, AppUserRole.DEVELOPER, AppUserRole.SUBMITTER, AppUserRole.NONE};
        return userRoles;
    }

    public AppUsersResponseDto getDataForRoleManagement() {
        AppUser user = getUserFromContext().orElseThrow();
        List<AppUser> allUsers = appUserRepository.findAll();
        if(user.isDemo()) {
            return getDemoDataForRoleManagement(allUsers);
        }
        else {
            return getRegularDataForRoleManagement(allUsers);
        }
    }
    private AppUsersResponseDto getRegularDataForRoleManagement(List<AppUser> allUsers) {
        return new AppUsersResponseDto(
                allUsers,
                allUsers.stream()
                        .filter(user -> user.getSRole() != null && !user.getSRole().equals("Admin"))
                        .collect(Collectors.toList()),
                getNonAdminAndNonDemoRoles()
        );
    }
    private AppUsersResponseDto getDemoDataForRoleManagement(List<AppUser> allUsers) {
        return new AppUsersResponseDto(
                allUsers.stream()
                        .filter(user -> user.isDemo() != null && user.isDemo())
                        .collect(Collectors.toList()),
                allUsers.stream()
                        .filter(user -> user.isDemo() != null && user.isDemo())
                        .filter(user -> user.getSRole() != null && !user.getSRole().equals("Admin"))
                        .collect(Collectors.toList()),
                getNonAdminAndNonDemoRoles()
        );
    }
}
