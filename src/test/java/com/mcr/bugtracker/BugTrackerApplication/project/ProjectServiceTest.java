package com.mcr.bugtracker.BugTrackerApplication.project;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        doNothing().when(projectService1).validateUserPermissionForProjectEdit(any());
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
        verify(projectService1).validateUserPermissionForProjectEdit(any());
        verify(appUserService).getAllUsersNotInProject(any());
        verify(projectDtoMapper).apply(project);
        verify(appUserDtoMapper).apply(projectManagerInCharge);
        assertEquals(1, projectEditViewDto.getProjectManagers().size());
        assertEquals(2, projectEditViewDto.getProjectPersonnel().size());
        verify(appUserDtoMapper, times(2)).apply(projectManagerInPersonnel);
    }

    @Test
    void saveResponseElements() {
    }

    @Test
    void deleteProjectById() {
    }
}