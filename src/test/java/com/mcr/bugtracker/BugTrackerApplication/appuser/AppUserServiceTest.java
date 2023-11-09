package com.mcr.bugtracker.BugTrackerApplication.appuser;

import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiEmailTakenException;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiNotFoundException;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiPasswordDoesntMatchException;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiRequestException;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.registration.token.ConfirmationTokenService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AppUserServiceTest {
    @Mock
    AppUserRepository appUserRepository;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    ConfirmationTokenService confirmationTokenService;
    @Mock
    UserProfileDtoMapper userProfileDtoMapper;
    @Mock
    AppUserDtoMapper appUserDtoMapper;
    AppUserService appUserService;
    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        appUserService = new AppUserService(appUserRepository, bCryptPasswordEncoder, confirmationTokenService, userProfileDtoMapper, appUserDtoMapper);
    }

    @AfterEach
    void tearUp() throws Exception {
        autoCloseable.close();
    }
    @Test
    public void checkIfEmailTakenOrNotConfirmedTest_EmailNotTaken() {
        //given
        AppUser appUser = new AppUser();
        appUser.setEmail("123");
        when(appUserRepository.findByEmail(appUser.getEmail())).thenReturn(Optional.empty());
        //when
        appUserService.checkIfEmailTakenOrNotConfirmed(appUser);
        //then
    }
    @Test
    public void checkIfEmailTakenOrNotConfirmedTest_EmailNotConfirmed() {
        //given
        AppUser appUser = new AppUser();
        appUser.setEmail("123");
        when(appUserRepository.findByEmail(appUser.getEmail())).thenReturn(Optional.of(appUser));
        //when
        ApiRequestException exception =
                assertThrows(ApiRequestException.class, () -> appUserService.checkIfEmailTakenOrNotConfirmed(appUser));
        //then
        assertEquals(exception.getMessage(), "Confirm email");
    }
    @Test
    public void checkIfEmailTakenOrNotConfirmedTest_EmailTaken() {
        //given
        AppUser appUser = new AppUser();
        appUser.setEmail("123");
        appUser.setEnabled(true);
        when(appUserRepository.findByEmail(appUser.getEmail())).thenReturn(Optional.of(appUser));
        //when
        ApiRequestException exception =
                assertThrows(ApiRequestException.class, () -> appUserService.checkIfEmailTakenOrNotConfirmed(appUser));
        //then
        assertEquals(exception.getMessage(), "Email already taken");
    }
    @Test
    public void signUpUserTest() {
        //given
        AppUser user = new AppUser();
        user.setPassword("123");
        when(bCryptPasswordEncoder.encode("123")).thenReturn("hash");
        //when
        appUserService.signUpUser(user);
        //then
        verify(bCryptPasswordEncoder).encode("123");
        verify(appUserRepository).save(user);
        assertEquals(user.getPassword(), "hash");
    }
    @Test
    public void enableAppUserTest() {
        //given
        AppUser appUser = new AppUser();
        //when
        appUserService.enableAppUser(appUser);
        //then
        verify(appUserRepository).save(appUser);
        assertEquals(true, appUser.getEnabled());
    }
    @Test
    public void changeUsersRoleTest() {
        //given
        AppUser appUser1 = new AppUser();
        AppUser appUser2 = new AppUser();
        appUser1.setEmail("email1");
        appUser2.setEmail("email2");
        AppUserRole role = AppUserRole.NONE;
        AppUserRoleAssignmentRequest request = new AppUserRoleAssignmentRequest(Arrays.asList(appUser1.getEmail(), appUser2.getEmail()), role.getName());
        when(appUserRepository.findByEmail(appUser1.getEmail())).thenReturn(Optional.of(appUser1));
        when(appUserRepository.findByEmail(appUser2.getEmail())).thenReturn(Optional.of(appUser2));
        //when
        appUserService.changeUsersRole(request);
        //then
        verify(appUserRepository).findByEmail("email1");
        verify(appUserRepository).findByEmail("email2");
        verify(appUserRepository).save(appUser1);
        verify(appUserRepository).save(appUser2);
        assertEquals(role, appUser1.getAppUserRole());
        assertEquals(role, appUser2.getAppUserRole());
        assertEquals(role.getName(), appUser1.getSRole());
        assertEquals(role.getName(), appUser2.getSRole());
    }
    @Test
    public void changeUsersRoleTest_notValidRoleName() {
        //given
        AppUser appUser1 = new AppUser();
        AppUser appUser2 = new AppUser();
        appUser1.setEmail("email1");
        appUser2.setEmail("email2");
        AppUserRoleAssignmentRequest request = new AppUserRoleAssignmentRequest(Arrays.asList(appUser1.getEmail(), appUser2.getEmail()), "qwerty");
        //when
        //then
        assertThrows(NoSuchElementException.class, () -> appUserService.changeUsersRole(request));
    }
    @Test
    public void findRoleObjectTest() {
        //given
        String role = AppUserRole.DEVELOPER.getName();
        //when
        AppUserRole appUserRole = appUserService.findRoleObject(role);
        //then
        assertEquals(AppUserRole.DEVELOPER, appUserRole);
    }
    @Test
    public void findRoleObjectTest_NotValidRoleName() {
        //given
        String role = "qwerty";
        //when
        //then
        assertThrows(NoSuchElementException.class, () -> appUserService.findRoleObject(role));
    }
    @Test
    public void validatePasswordMatchAndSetNewPasswordTest() {
        //given
        String oldPassword = "12345";
        AppUser user = new AppUser();
        user.setPassword("12345");
        AppUserService appUserService1 = spy(appUserService);
        doReturn(Optional.of(user)).when(appUserService1).getUserFromContext();
        when(bCryptPasswordEncoder.matches(oldPassword.subSequence(0, oldPassword.length()), user.getPassword())).thenReturn(true);
        String newPassword = "qwerty";
        //when
        appUserService1.validatePasswordMatchAndSetNewPassword(oldPassword, newPassword);
        //then
        verify(bCryptPasswordEncoder).encode(newPassword.subSequence(0, newPassword.length()));
        verify(appUserRepository).save(user);
    }
    @Test
    public void validatePasswordMatchAndSetNewPasswordTest_PasswordsDontMatch() {
        //given
        String oldPassword = "12345";
        AppUser user = new AppUser();
        user.setPassword("321541235");
        AppUserService appUserService1 = spy(appUserService);
        doReturn(Optional.of(user)).when(appUserService1).getUserFromContext();
        when(bCryptPasswordEncoder.matches(oldPassword.subSequence(0, oldPassword.length()), user.getPassword())).thenReturn(false);
        String newPassword = "qwerty";
        //when
        //then
        assertThrows(ApiPasswordDoesntMatchException.class, () ->
                appUserService1.validatePasswordMatchAndSetNewPassword(oldPassword, newPassword));
    }
    @Test
    public void getDataForUserProfileTest() {
        //given
        AppUser appUser = new AppUser();
        AppUserService appUserService1 = spy(appUserService);
        doReturn(Optional.of(appUser)).when(appUserService1).getUserFromContext();
        //when
        appUserService1.getDataForUserProfile();
        //then
        verify(userProfileDtoMapper).apply(appUser);
    }
    @Test
    public void validateEmailChangeAndSetNewEmailTest_EmailIsTaken() {
        //given
        AppUserService appUserService1 = spy(appUserService);
        doReturn(true).when(appUserService1).isEmailTaken("123");
        //when
        //then
        assertThrows(ApiEmailTakenException.class, () ->
                appUserService1.validateEmailChangeAndSetNewEmail("123", "aaa"));
    }
    @Test
    public void validateEmailChangeAndSetNewEmailTest_PasswordNotCorrect() {
        //given
        AppUserService appUserService1 = spy(appUserService);
        doReturn(false).when(appUserService1).isEmailTaken("123");
        doReturn(false).when(appUserService1).isPasswordCorrect("aaa");
        //when
        //then
        assertThrows(ApiPasswordDoesntMatchException.class, () ->
                appUserService1.validateEmailChangeAndSetNewEmail("123", "aaa"));
    }
    @Test
    public void validateEmailChangeAndSetNewEmailTest_ValidCredentials() {
        //given
        AppUserService appUserService1 = spy(appUserService);
        doReturn(false).when(appUserService1).isEmailTaken("123");
        doReturn(true).when(appUserService1).isPasswordCorrect("aaa");
        doNothing().when(appUserService1).changeEmail("123");
        //when
        appUserService1.validateEmailChangeAndSetNewEmail("123", "aaa");
        //then
        verify(appUserService1).changeEmail("123");
    }
    @Test
    public void changeEmailTest() {
        //given
        AppUser appUser = new AppUser();
        AppUserService appUserService1 = spy(appUserService);
        doReturn(Optional.of(appUser)).when(appUserService1).getUserFromContext();
        //when
        appUserService1.changeEmail("123");
        //then
        assertEquals("123", appUser.getEmail());
        verify(appUserRepository).save(appUser);
    }
    @Test
    public void getDataForDashboardViewTest() {
        //given
        AppUser appUser = new AppUser();
        appUser.setSRole("Developer");
        AppUserService appUserService1 = spy(appUserService);
        doReturn(Optional.of(appUser)).when(appUserService1).getUserFromContext();
        doReturn(null).when(appUserService1).getDataForDeveloperDashboardView(appUser);
        //when
        appUserService1.getDataForDashboardView();
        //then
        verify(appUserService1).getDataForDeveloperDashboardView(appUser);
    }
    @Test
    public void getDataForDashboardViewTest_NoSuchValue() {
        //given
        AppUser appUser = new AppUser();
        appUser.setSRole("dsfdsafdfsdfsdfsfds");
        AppUserService appUserService1 = spy(appUserService);
        doReturn(Optional.of(appUser)).when(appUserService1).getUserFromContext();
        //when
        //then
        assertThrows(ApiNotFoundException.class, appUserService1::getDataForDashboardView);
    }
    @Test
    public void getDataForNoRoleDashboardViewTest() {
        //given
        AppUser appUser = new AppUser();
        Project project = new Project();
        project.setName("name");
        project.setDescription("description");
        appUser.setAssignedProjects(List.of(project));
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        dashboardViewDto.setProjectName("name");
        dashboardViewDto.setProjectDescription("description");
        //when
        DashboardViewDto dashboardViewDto2 = appUserService.getDataForNoRoleDashboardView(appUser);
        //then
        assertEquals(dashboardViewDto.getProjectName(), dashboardViewDto2.getProjectName());
        assertEquals(dashboardViewDto.getProjectDescription(), dashboardViewDto2.getProjectDescription());
    }
    @Test
    public void getDataForDeveloperDashboardViewTest() {
        //given
        AppUser appUser = new AppUser();
        Project project = new Project();
        Ticket ticket = new Ticket();
        ticket.setTitle("ticket title");
        ticket.setDescription("ticket description");
        project.setName("project name");
        project.setDescription("project description");
        appUser.setAssignedProjects(List.of(project));
        appUser.setAssignedTickets(List.of(ticket));
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        dashboardViewDto.setProjectName("project name");
        dashboardViewDto.setProjectDescription("project description");
        dashboardViewDto.setTicketTitle("ticket title");
        dashboardViewDto.setTicketDescription("ticket description");
        //when
        DashboardViewDto dashboardViewDto2 = appUserService.getDataForDeveloperDashboardView(appUser);
        //then
        assertEquals(dashboardViewDto.getProjectName(), dashboardViewDto2.getProjectName());
        assertEquals(dashboardViewDto.getProjectDescription(), dashboardViewDto2.getProjectDescription());
        assertEquals(dashboardViewDto.getTicketTitle(), dashboardViewDto2.getTicketTitle());
        assertEquals(dashboardViewDto.getTicketDescription(), dashboardViewDto2.getTicketDescription());
    }
    @Test
    public void getDataForSubmitterDashboardViewTest() {
        //given
        AppUser appUser = new AppUser();
        Project project = new Project();
        Ticket ticket = new Ticket();
        ticket.setTitle("ticket title");
        ticket.setDescription("ticket description");
        project.setName("project name");
        project.setDescription("project description");
        appUser.setAssignedProjects(List.of(project));
        appUser.setSubmittedTickets(List.of(ticket));
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        dashboardViewDto.setProjectName("project name");
        dashboardViewDto.setProjectDescription("project description");
        dashboardViewDto.setTicketTitle("ticket title");
        dashboardViewDto.setTicketDescription("ticket description");
        //when
        DashboardViewDto dashboardViewDto2 = appUserService.getDataForSubmitterDashboardView(appUser);
        //then
        assertEquals(dashboardViewDto.getProjectName(), dashboardViewDto2.getProjectName());
        assertEquals(dashboardViewDto.getProjectDescription(), dashboardViewDto2.getProjectDescription());
        assertEquals(dashboardViewDto.getTicketTitle(), dashboardViewDto2.getTicketTitle());
        assertEquals(dashboardViewDto.getTicketDescription(), dashboardViewDto2.getTicketDescription());
    }
    @Test
    public void getDataForProjectManagerDashboardViewTest_ManagedProjectChosen() {
        //given
        AppUser appUser = new AppUser();
        Project project = new Project();
        project.setName("project name");
        project.setDescription("project description");
        appUser.setManagedProjects(List.of(project));
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        dashboardViewDto.setProjectName("project name");
        dashboardViewDto.setProjectDescription("project description");
        //when
        DashboardViewDto dashboardViewDto2 = appUserService.getDataForProjectManagerDashboardView(appUser);
        //then
        assertEquals(dashboardViewDto.getProjectName(), dashboardViewDto2.getProjectName());
        assertEquals(dashboardViewDto.getProjectDescription(), dashboardViewDto2.getProjectDescription());
    }
    @Test
    public void getDataForProjectManagerDashboardViewTest_AssignedProjectChosen() {
        //given
        AppUser appUser = new AppUser();
        Project project = new Project();
        project.setName("project name");
        project.setDescription("project description");
        appUser.setAssignedProjects(List.of(project));
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        dashboardViewDto.setProjectName("project name");
        dashboardViewDto.setProjectDescription("project description");
        //when
        DashboardViewDto dashboardViewDto2 = appUserService.getDataForProjectManagerDashboardView(appUser);
        //then
        assertEquals(dashboardViewDto.getProjectName(), dashboardViewDto2.getProjectName());
        assertEquals(dashboardViewDto.getProjectDescription(), dashboardViewDto2.getProjectDescription());
    }
    @Test
    public void getDataForProjectManagerDashboardViewTest_SubmittedTicketReturned() {
        //given
        AppUser appUser = new AppUser();
        Ticket ticket = new Ticket();
        ticket.setTitle("ticket title");
        ticket.setDescription("ticket description");
        appUser.setSubmittedTickets(List.of(ticket));
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        dashboardViewDto.setTicketTitle("ticket title");
        dashboardViewDto.setTicketDescription("ticket description");
        //when
        DashboardViewDto dashboardViewDto2 = appUserService.getDataForProjectManagerDashboardView(appUser);
        //then
        assertEquals(dashboardViewDto.getTicketTitle(), dashboardViewDto2.getTicketTitle());
        assertEquals(dashboardViewDto.getTicketDescription(), dashboardViewDto2.getTicketDescription());
    }
    @Test
    public void getDataForProjectManagerDashboardViewTest_OneOfTicketsInProjectReturned() {
        //given
        AppUser appUser = new AppUser();
        Ticket ticket = new Ticket();
        Project project = new Project();
        ticket.setTitle("ticket title");
        ticket.setDescription("ticket description");
        project.setTickets(List.of(ticket));
        appUser.setManagedProjects(List.of(project));
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        dashboardViewDto.setTicketTitle("ticket title");
        dashboardViewDto.setTicketDescription("ticket description");
        //when
        DashboardViewDto dashboardViewDto2 = appUserService.getDataForProjectManagerDashboardView(appUser);
        //then
        assertEquals(dashboardViewDto.getTicketTitle(), dashboardViewDto2.getTicketTitle());
        assertEquals(dashboardViewDto.getTicketDescription(), dashboardViewDto2.getTicketDescription());
    }
    @Test
    public void setValuesFromRandomTicketTest() {
        //given
        Ticket ticket = new Ticket();
        ticket.setTitle("ticket title");
        ticket.setDescription("ticket description");
        List<Ticket> tickets = List.of(ticket);
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        //when
        appUserService.setValuesFromRandomTicket(dashboardViewDto, tickets);
        //then
        assertEquals(ticket.getTitle(), dashboardViewDto.getTicketTitle());
        assertEquals(ticket.getDescription(), dashboardViewDto.getTicketDescription());
    }
    @Test
    public void setValuesFromRandomTicketTest_DefaultValuesReturned() {
        //given
        Ticket ticket = new Ticket();
        List<Ticket> tickets = List.of(ticket);
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        //when
        appUserService.setValuesFromRandomTicket(dashboardViewDto, tickets);
        //then
        assertEquals("Title not specified", dashboardViewDto.getTicketTitle());
        assertEquals("Description not specified", dashboardViewDto.getTicketDescription());
    }
    @Test
    public void setValuesFromRandomProjectTest() {
        //given
        Project project = new Project();
        project.setName("project name");
        project.setDescription("project description");
        List<Project> projects = List.of(project);
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        //when
        appUserService.setValuesFromRandomProject(dashboardViewDto, projects);
        //then
        assertEquals(project.getName(), dashboardViewDto.getProjectName());
        assertEquals(project.getDescription(), dashboardViewDto.getProjectDescription());
    }
    @Test
    public void setValuesFromRandomProjectTest_DefaultValuesReturned() {
        //given
        Project project = new Project();
        List<Project> projects = List.of(project);
        DashboardViewDto dashboardViewDto = new DashboardViewDto();
        //when
        appUserService.setValuesFromRandomProject(dashboardViewDto, projects);
        //then
        assertEquals("Name not specified", dashboardViewDto.getProjectName());
        assertEquals("Description not specified", dashboardViewDto.getProjectDescription());
    }
    @Test
    public void getDataForRoleManagementTest() {
        //given
        AppUserService appUserService1 = spy(appUserService);
        AppUser appUser = new AppUser();
        doReturn(Optional.of(appUser)).when(appUserService1).getUserFromContext();
        List<AppUser> appUsers = new ArrayList<>();
        when(appUserRepository.findAll()).thenReturn(appUsers);
        RoleManagementDto roleManagementDto = new RoleManagementDto();
        doReturn(roleManagementDto).when(appUserService1).getDataForRoleManagement(appUsers);
        //when
        RoleManagementDto roleManagementDto1 = appUserService1.getDataForRoleManagement();
        //then
        verify(appUserRepository).findAll();
        assertEquals(roleManagementDto, roleManagementDto1);
    }
    @Test
    public void getDataForRoleManagementTest_DemoUser() {
        //given
        AppUserService appUserService1 = spy(appUserService);
        AppUser appUser = new AppUser();
        appUser.setDemo(true);
        doReturn(Optional.of(appUser)).when(appUserService1).getUserFromContext();
        List<AppUser> appUsers = new ArrayList<>();
        when(appUserRepository.findAll()).thenReturn(appUsers);
        RoleManagementDto roleManagementDto = new RoleManagementDto();
        doReturn(roleManagementDto).when(appUserService1).getDataForRoleManagement(appUsers);
        //when
        RoleManagementDto roleManagementDto1 = appUserService1.getDataForRoleManagement();
        //then
        verify(appUserRepository).findAll();
        assertEquals(roleManagementDto, roleManagementDto1);
    }
    @Test
    public void getRegularDataForRoleManagementTest() {
        //given
        AppUserService appUserService1 = spy(appUserService);
        AppUser appUser = new AppUser();
        appUser.setSRole("Admin");
        appUser.setEmail("Admin Email");
        AppUser appUser1 = new AppUser();
        appUser1.setEmail("Non Admin email");
        List<AppUser> appUsers = Arrays.asList(appUser, appUser1);
        //when
        RoleManagementDto roleManagementDto = appUserService1.getDataForRoleManagement(appUsers);
        //then
        verify(appUserDtoMapper).apply(appUser);
        verify(appUserDtoMapper).apply(appUser1);
        verify(appUserService1).getNonAdminAndNonDemoRoles();
        assertEquals(2, roleManagementDto.getAllUsers().size());
        assertTrue(roleManagementDto.getNonAdminUsersEmails().contains("Non Admin email"));
        assertFalse(roleManagementDto.getNonAdminUsersEmails().contains("Admin email"));
    }
    @Test
    public void getAllUsersNotInProjectTest_NonDemoManagerNoUsersReturned() {
        //given
        Project project = new Project();
        AppUser appUser = new AppUser();
        appUser.setId(711L);
        project.setProjectPersonnel(List.of(appUser));
        AppUser appUser1 = new AppUser();
        appUser1.setId(712L);
        project.setProjectManager(appUser1);
        AppUser appUser2 = new AppUser();
        appUser2.setSRole("Admin");
        appUser2.setId(713L);
        when(appUserRepository.findAll()).thenReturn(Arrays.asList(appUser, appUser1, appUser2));
        //when
        List<AppUserDto> appUserDtos = appUserService.getAllUsersNotInProject(project);
        //then
        assertEquals(0, appUserDtos.size());
    }
    @Test
    public void getAllUsersNotInProjectTest_NonDemoManagerValidUsersReturned() {
        //given
        Project project = new Project();
        project.setProjectManager(new AppUser());
        AppUser appUser = new AppUser();
        appUser.setId(711L);
        AppUser appUser1 = new AppUser();
        appUser1.setId(712L);
        AppUser appUser2 = new AppUser();
        appUser2.setId(713L);
        when(appUserRepository.findAll()).thenReturn(Arrays.asList(appUser, appUser1, appUser2));
        //when
        List<AppUserDto> appUserDtos = appUserService.getAllUsersNotInProject(project);
        //then
        assertEquals(3, appUserDtos.size());
        verify(appUserDtoMapper).apply(appUser);
        verify(appUserDtoMapper).apply(appUser1);
        verify(appUserDtoMapper).apply(appUser2);
    }
    @Test
    public void getAllUsersNotInProjectTest_DemoManagerValidNonDemoUsersReturned() {
        //given
        Project project = new Project();
        AppUser projectManager = new AppUser();
        projectManager.setDemo(true);
        project.setProjectManager(projectManager);
        AppUser appUser = new AppUser();
        appUser.setId(711L);
        AppUser appUser1 = new AppUser();
        appUser1.setId(712L);
        AppUser appUser2 = new AppUser();
        appUser2.setId(713L);
        when(appUserRepository.findAll()).thenReturn(Arrays.asList(appUser, appUser1, appUser2));
        //when
        List<AppUserDto> appUserDtos = appUserService.getAllUsersNotInProject(project);
        //then
        assertEquals(0, appUserDtos.size());
    }
    @Test
    public void getAllUsersNotInProjectTest_DemoManagerValidDemoUsersReturned() {
        //given
        Project project = new Project();
        AppUser projectManager = new AppUser();
        projectManager.setDemo(true);
        project.setProjectManager(projectManager);
        AppUser appUser = new AppUser();
        appUser.setId(711L);
        appUser.setDemo(true);
        AppUser appUser1 = new AppUser();
        appUser1.setId(712L);
        appUser1.setDemo(true);
        AppUser appUser2 = new AppUser();
        appUser2.setId(713L);
        appUser2.setDemo(true);
        when(appUserRepository.findAll()).thenReturn(Arrays.asList(appUser, appUser1, appUser2));
        //when
        List<AppUserDto> appUserDtos = appUserService.getAllUsersNotInProject(project);
        //then
        assertEquals(3, appUserDtos.size());
        verify(appUserDtoMapper).apply(appUser);
        verify(appUserDtoMapper).apply(appUser1);
        verify(appUserDtoMapper).apply(appUser2);
    }
}