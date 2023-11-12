package com.mcr.bugtracker.BugTrackerApplication.project;

import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiForbiddenException;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiNotFoundException;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserDtoMapper;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import com.mcr.bugtracker.BugTrackerApplication.ticket.TicketForProjectViewDtoMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    ProjectRepository projectRepository;
    @Mock
    AppUserService appUserService;
    @Mock
    AllProjectsViewMapper allProjectsViewMapper;
    @Mock
    AppUserDtoMapper appUserDtoMapper;
    @Mock
    ProjectDtoMapper projectDtoMapper;
    @Mock
    TicketForProjectViewDtoMapper ticketForProjectViewDtoMapper;
    ProjectService projectService;
    AutoCloseable autoCloseable;
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        projectService = new ProjectService(projectRepository, appUserService, allProjectsViewMapper, appUserDtoMapper,
                projectDtoMapper, ticketForProjectViewDtoMapper);
    }

    @AfterEach
    void tearUp() throws Exception {
        autoCloseable.close();
    }
    @Test
    void findAllProjectsAssignedToUserTest_DemoAdmin() {
        //given
        AppUser appUser = new AppUser();
        appUser.setSRole("Admin");
        appUser.setDemo(true);
        doReturn(Optional.of(appUser)).when(appUserService).getUserFromContext();
        Project projectWithDemoManager = new Project();
        projectWithDemoManager.setProjectManager(appUser);
        Project projectWithoutDemoManager = new Project();
        projectWithoutDemoManager.setProjectManager(new AppUser());
        when(projectRepository.findAll()).thenReturn(Arrays.asList(projectWithoutDemoManager, projectWithDemoManager));
        //when
        List<AllProjectsViewDto> allProjectsViewDtoList = projectService.findAllProjectsAssignedToUser();
        //then
        assertEquals(1, allProjectsViewDtoList.size());
        verify(allProjectsViewMapper).apply(projectWithDemoManager);
    }
    @Test
    void findAllProjectsAssignedToUserTest_NonDemoAdmin() {
        //given
        AppUser appUser = new AppUser();
        appUser.setSRole("Admin");
        doReturn(Optional.of(appUser)).when(appUserService).getUserFromContext();
        Project project = new Project();
        project.setId(624L);
        Project project1 = new Project();
        project1.setId(653L);
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project));
        //when
        List<AllProjectsViewDto> allProjectsViewDtoList = projectService.findAllProjectsAssignedToUser();
        //then
        assertEquals(2, allProjectsViewDtoList.size());
        verify(allProjectsViewMapper).apply(project);
        verify(allProjectsViewMapper).apply(project1);
    }
    @Test
    void findAllProjectsAssignedToUserTest_NonAdmin() {
        //given
        AppUser appUser = new AppUser();
        doReturn(Optional.of(appUser)).when(appUserService).getUserFromContext();
        Project project = new Project();
        project.setId(624L);
        appUser.setManagedProjects(List.of(project));
        Project project1 = new Project();
        project1.setId(653L);
        appUser.setAssignedProjects(List.of(project1));
        when(projectRepository.findAll()).thenReturn(Arrays.asList(project1, project));
        //when
        List<AllProjectsViewDto> allProjectsViewDtoList = projectService.findAllProjectsAssignedToUser();
        //then
        assertEquals(2, allProjectsViewDtoList.size());
        verify(allProjectsViewMapper).apply(project);
        verify(allProjectsViewMapper).apply(project1);
    }

    @Test
    void getDataForProjectDetailsViewTest() {
        //given
        ProjectService projectService1 = spy(projectService);
        doNothing().when(projectService1).validateProjectExistence(any());
        doNothing().when(projectService1).validateUserPermissionForProjectDetails(any());
        AppUser appUser = new AppUser();
        appUser.setWholeName("aaa");
        appUser.setEmail("bbb");
        Project project = new Project();
        project.setProjectManager(appUser);
        project.setProjectPersonnel(List.of(new AppUser()));
        project.setTickets(List.of(new Ticket()));
        when(projectRepository.findById(any())).thenReturn(Optional.of(project));
        //when
        ProjectDetailsViewDto projectDetailsViewDto = projectService1.getDataForProjectDetailsView(any());
        //then
        verify(projectService1).validateProjectExistence(any());
        verify(projectService1).validateUserPermissionForProjectDetails(any());
        verify(projectDtoMapper).apply(project);
        assertEquals("aaa", projectDetailsViewDto.getProjectManagerName());
        assertEquals("bbb", projectDetailsViewDto.getProjectManagerEmail());
        verify(appUserDtoMapper).apply(any());
        verify(ticketForProjectViewDtoMapper).apply(any());
        assertEquals(1, projectDetailsViewDto.getProjectPersonnel().size());
        assertEquals(1, projectDetailsViewDto.getTickets().size());
    }

    @Test
    void getDataForProjectEditViewTest() {
        //given
        ProjectService projectService1 = spy(projectService);
        doNothing().when(projectService1).validateProjectExistence(any());
        doNothing().when(projectService1).validateUserPermissionForProjectEditAndDelete(any());
        doReturn(null).when(appUserService).getAllUsersNotInProject(any());
        AppUser projectManagerInCharge = new AppUser();
        projectManagerInCharge.setId(132L);
        projectManagerInCharge.setSRole("Project manager");
        AppUser projectManagerInPersonnel = new AppUser();
        projectManagerInPersonnel.setId(231L);
        projectManagerInPersonnel.setSRole("Project manager");
        Project project = new Project();
        project.setProjectManager(projectManagerInCharge);
        project.setProjectPersonnel(Arrays.asList(projectManagerInPersonnel, new AppUser()));
        when(projectRepository.findById(any())).thenReturn(Optional.of(project));
        //when
        ProjectEditViewDto projectEditViewDto = projectService1.getDataForProjectEditView(any());
        //then
        verify(projectService1).validateProjectExistence(any());
        verify(projectService1).validateUserPermissionForProjectEditAndDelete(any());
        verify(appUserService).getAllUsersNotInProject(any());
        verify(projectDtoMapper).apply(project);
        verify(appUserDtoMapper).apply(projectManagerInCharge);
        assertEquals(1, projectEditViewDto.getProjectManagers().size());
        assertEquals(2, projectEditViewDto.getProjectPersonnel().size());
        verify(appUserDtoMapper, times(2)).apply(projectManagerInPersonnel);
    }
    @Test
    void validateUserPermissionForProjectEditAndDeleteTest_ProjectManagerId() {
        //given
        AppUser appUser = new AppUser();
        appUser.setId(555L);
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(appUser));
        //when
        projectService.validateUserPermissionForProjectEditAndDelete(555L);
        //then
        assertDoesNotThrow(() -> projectService.validateUserPermissionForProjectEditAndDelete(555L));
    }
    @Test
    void validateUserPermissionForProjectEditAndDeleteTest_Admin() {
        //given
        AppUser appUser = new AppUser();
        appUser.setSRole("Admin");
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(appUser));
        //when
        projectService.validateUserPermissionForProjectEditAndDelete(555L);
        //then
        assertDoesNotThrow(() -> projectService.validateUserPermissionForProjectEditAndDelete(555L));
    }
    @Test
    void validateUserPermissionForProjectEditAndDeleteTest_ShouldThrow() {
        //given
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(new AppUser()));
        //when
        //then
        assertThrows(ApiForbiddenException.class,
                () -> projectService.validateUserPermissionForProjectEditAndDelete(555L));
    }
    @Test
    void validateProjectExistenceTest() {
        //given
        when(projectRepository.findById(any())).thenReturn(Optional.of(new Project()));
        //when
        projectService.validateProjectExistence(any());
        //then
        assertDoesNotThrow(() -> projectService.validateProjectExistence(any()));
    }
    @Test
    void validateProjectExistenceTest_ShouldThrow() {
        //given
        when(projectRepository.findById(any())).thenReturn(Optional.empty());
        //when
        //then
        assertThrows(ApiNotFoundException.class,
                () -> projectService.validateProjectExistence(any()));
    }
    @Test
    void validateUserPermissionForProjectDetailsTest_ProjectManager() {
        //given
        Project project = new Project();
        AppUser appUser = new AppUser();
        project.setProjectManager(appUser);
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(appUser));
        //when
        projectService.validateUserPermissionForProjectDetails(project);
        //then
        assertDoesNotThrow(() -> projectService.validateUserPermissionForProjectDetails(project));
    }
    @Test
    void validateUserPermissionForProjectDetailsTest_Admin() {
        //given
        Project project = new Project();
        AppUser appUser = new AppUser();
        appUser.setSRole("Admin");
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(appUser));
        //when
        projectService.validateUserPermissionForProjectDetails(project);
        //then
        assertDoesNotThrow(() -> projectService.validateUserPermissionForProjectDetails(project));
    }
    @Test
    void validateUserPermissionForProjectDetailsTest_ShouldThrow() {
        //given
        Project project = new Project();
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(new AppUser()));
        //when
        //then
        assertThrows(ApiForbiddenException.class,
                () -> projectService.validateUserPermissionForProjectDetails(project));
    }
    @Test
    void saveResponseElementsTest() {
        //given
        ProjectService projectService1 = spy(projectService);
        Project project = new Project();
        AppUser appUser = new AppUser();
        ProjectEditViewResponse projectEditViewResponse = new ProjectEditViewResponse();
        projectEditViewResponse.setProject(project);
        projectEditViewResponse.setProjectPersonnel(List.of(appUser));
        projectEditViewResponse.setCurrentManager(appUser);
        doNothing().when(projectService1).validateProjectExistence(any());
        doNothing().when(projectService1).validateUserPermissionForProjectEditAndDelete(any());
        //when
        projectService1.saveResponseElements(projectEditViewResponse);
        //then
        verify(projectService1).validateProjectExistence(any());
        verify(projectService1).validateUserPermissionForProjectEditAndDelete(any());
        assertEquals(project.getProjectPersonnel(), List.of(appUser));
        assertEquals(project.getProjectManager(), appUser);
        verify(projectRepository).save(project);
    }

    @Test
    void deleteProjectById() {
        //given
        ProjectService projectService1 = spy(projectService);
        Project project = new Project();
        when(projectRepository.findById(any())).thenReturn(Optional.of(project));
        doNothing().when(projectService1).validateProjectExistence(any());
        doNothing().when(projectService1).validateUserPermissionForProjectEditAndDelete(any());
        //when
        projectService1.deleteProjectById(any());
        //then
        verify(projectService1).validateProjectExistence(any());
        verify(projectService1).validateUserPermissionForProjectEditAndDelete(any());
        verify(projectRepository).findById(any());
        verify(projectRepository).deleteById(any());
    }
}