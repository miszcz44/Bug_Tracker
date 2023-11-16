package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserDtoMapper;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import com.mcr.bugtracker.BugTrackerApplication.ticket.commentary.CommentaryService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryField;
import com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField.TicketHistoryFieldService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiForbiddenException;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TicketServiceTest {
    @Mock
    TicketRepository ticketRepository;
    @Mock
    AppUserRepository appUserRepository;
    @Mock
    AppUserService appUserService;
    @Mock
    TicketHistoryFieldService ticketHistoryFieldService;
    @Mock
    AllTicketsViewMapper allTicketsViewMapper;
    @Mock
    TicketForTicketDetailsViewDtoMapper ticketForTicketDetailsViewDtoMapper;
    @Mock
    TicketForTicketEditViewMapper ticketForTicketEditViewMapper;
    @Mock
    AppUserDtoMapper appUserDtoMapper;
    @Mock CommentaryService commentaryService;
    TicketService ticketService;
    AutoCloseable autoCloseable;
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        ticketService = new TicketService(ticketRepository, appUserRepository, commentaryService, appUserService,
                ticketHistoryFieldService, allTicketsViewMapper, ticketForTicketDetailsViewDtoMapper,
                ticketForTicketEditViewMapper, appUserDtoMapper);
    }

    @AfterEach
    void tearUp() throws Exception {
        autoCloseable.close();
    }
    @Test
    void getAllTicketsConnectedToUserTest_DemoAdmin() {
        //given
        AppUser demoAdmin = new AppUser();
        demoAdmin.setDemo(true);
        demoAdmin.setSRole("Admin");
        Ticket ticket = new Ticket();
        ticket.setSubmitter(demoAdmin);
        Ticket ticket1 = new Ticket();
        ticket1.setSubmitter(new AppUser());
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(demoAdmin));
        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket, ticket1));
        //when
        List<AllTicketsViewDto> allTicketsViewDtos = ticketService.getAllTicketsConnectedToUser();
        //then
        verify(allTicketsViewMapper, times(1)).apply(any());
        assertEquals(1, allTicketsViewDtos.size());
    }
    @Test
    void getAllTicketsConnectedToUserTest_NonDemoAdmin() {
        //given
        AppUser admin = new AppUser();
        admin.setSRole("Admin");
        AppUser demo = new AppUser();
        demo.setDemo(true);
        Ticket ticket = new Ticket();
        ticket.setSubmitter(demo);
        Ticket ticket1 = new Ticket();
        ticket1.setSubmitter(new AppUser());
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(admin));
        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket, ticket1));
        //when
        List<AllTicketsViewDto> allTicketsViewDtos = ticketService.getAllTicketsConnectedToUser();
        //then
        verify(allTicketsViewMapper, times(2)).apply(any());
        assertEquals(2, allTicketsViewDtos.size());
    }
    @Test
    void getAllTicketsConnectedToUserTest_RegularUser() {
        //given
        AppUser appUser = new AppUser();
        Ticket ticket = new Ticket();
        Ticket ticket1 = new Ticket();
        appUser.setAssignedTickets(List.of(ticket));
        appUser.setSubmittedTickets(List.of(ticket1));
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(appUser));
        //when
        List<AllTicketsViewDto> allTicketsViewDtos = ticketService.getAllTicketsConnectedToUser();
        //then
        verify(allTicketsViewMapper, times(2)).apply(any());
        assertEquals(2, allTicketsViewDtos.size());
    }
    @Test
    void getDemandedDataForTicketDetailsViewTest() {
        //given
        TicketService ticketService1 = spy(ticketService);
        Ticket ticket = new Ticket();
        ticket.setId(2442L);
        TicketHistoryField ticketHistoryField = new TicketHistoryField();
        ticketHistoryField.setDateChanged(LocalDateTime.now());
        ticket.setTicketHistoryFields(Arrays.asList(ticketHistoryField, ticketHistoryField));
        doNothing().when(ticketService1).validateTicketExistence(ticket.getId());
        doNothing().when(ticketService1).validateUserPermissionForTicketDetails(ticket);
        when(commentaryService.getCommentsWithDemandedData(ticket.getComments())).thenReturn(List.of());
        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        //when
        TicketDetailsViewDto ticketDetailsViewDto = ticketService1.getDemandedDataForTicketDetailsView(ticket.getId());
        //then
        verify(ticketForTicketDetailsViewDtoMapper).apply(ticket);
        verify(commentaryService).getCommentsWithDemandedData(ticket.getComments());
        assertEquals(2, ticketDetailsViewDto.getTicketHistoryField().size());
    }
    @Test
    void validateUserPermissionForTicketDetailsTest_ShouldThrow() {
        //given
        AppUser appUser = new AppUser();
        appUser.setSRole("None");
        Ticket ticket = new Ticket();
        ticket.setProject(new Project());
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(appUser));
        //when
        //then
        assertThrows(ApiForbiddenException.class, () -> ticketService.validateUserPermissionForTicketDetails(ticket));
    }
    @Test
    void validateUserPermissionForTicketDetailsTest_UserSubmitter() {
        //given
        AppUser appUser = new AppUser();
        Ticket ticket = new Ticket();
        ticket.setSubmitter(appUser);
        ticket.setProject(new Project());
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(appUser));
        //when
        ticketService.validateUserPermissionForTicketDetails(ticket);
        //then
        assertDoesNotThrow(() -> ticketService.validateUserPermissionForTicketDetails(ticket));
    }
    @Test
    void validateUserPermissionForTicketDetailsTest_UserProjectManagerInCommand() {
        //given
        AppUser appUser = new AppUser();
        Ticket ticket = new Ticket();
        Project project = new Project();
        project.setProjectManager(appUser);
        ticket.setProject(project);
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(appUser));
        //when
        ticketService.validateUserPermissionForTicketDetails(ticket);
        //then
        assertDoesNotThrow(() -> ticketService.validateUserPermissionForTicketDetails(ticket));
    }
    @Test
    void validateUserPermissionForTicketDetailsTest_UserProjectManagerInPersonnel() {
        //given
        AppUser appUser = new AppUser();
        appUser.setSRole("Project manager");
        Ticket ticket = new Ticket();
        Project project = new Project();
        project.setProjectPersonnel(List.of(appUser));
        ticket.setProject(project);
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(appUser));
        //when
        ticketService.validateUserPermissionForTicketDetails(ticket);
        //then
        assertDoesNotThrow(() -> ticketService.validateUserPermissionForTicketDetails(ticket));
    }
    @Test
    void validateUserPermissionForTicketDetailsTest_UserDeveloper() {
        //given
        AppUser appUser = new AppUser();
        Ticket ticket = new Ticket();
        ticket.setAssignedDeveloper(appUser);
        ticket.setProject(new Project());
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(appUser));
        //when
        ticketService.validateUserPermissionForTicketDetails(ticket);
        //then
        assertDoesNotThrow(() -> ticketService.validateUserPermissionForTicketDetails(ticket));
    }
    @Test
    void validateUserPermissionForTicketDetailsTest_UserAdmin() {
        //given
        AppUser appUser = new AppUser();
        appUser.setSRole("Admin");
        Ticket ticket = new Ticket();
        ticket.setProject(new Project());
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(appUser));
        //when
        ticketService.validateUserPermissionForTicketDetails(ticket);
        //then
        assertDoesNotThrow(() -> ticketService.validateUserPermissionForTicketDetails(ticket));
    }
    @Test
    void getDataForTicketEditViewTest_DeveloperNull() {
        //given
        TicketService ticketService1 = spy(ticketService);
        Ticket ticket = new Ticket();
        ticket.setId(5231L);
        Project project = new Project();
        AppUser developer = new AppUser();
        developer.setSRole("Developer");
        project.setProjectPersonnel(Arrays.asList(developer, new AppUser()));
        ticket.setProject(project);
        doNothing().when(ticketService1).validateTicketExistence(ticket.getId());
        doNothing().when(ticketService1).validateUserPermissionForTicketEditAndDelete(ticket);
        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        //when
        TicketEditViewDto ticketEditViewDto = ticketService1.getDataForTicketEditView(ticket.getId());
        //then
        verify(ticketService1).validateTicketExistence(ticket.getId());
        verify(ticketService1).validateUserPermissionForTicketEditAndDelete(ticket);
        verify(ticketForTicketEditViewMapper).apply(ticket);
        assertNull(ticketEditViewDto.getDeveloper());
        assertEquals(1, ticketEditViewDto.getPossibleDevelopers().size());
    }
    @Test
    void getDataForTicketEditViewTest_DeveloperNotNull() {
        //given
        TicketService ticketService1 = spy(ticketService);
        Ticket ticket = new Ticket();
        ticket.setId(5231L);
        Project project = new Project();
        AppUser developer = new AppUser();
        developer.setSRole("Developer");
        ticket.setAssignedDeveloper(developer);
        project.setProjectPersonnel(Arrays.asList(developer, new AppUser()));
        ticket.setProject(project);
        doNothing().when(ticketService1).validateTicketExistence(ticket.getId());
        doNothing().when(ticketService1).validateUserPermissionForTicketEditAndDelete(ticket);
        when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
        //when
        TicketEditViewDto ticketEditViewDto = ticketService1.getDataForTicketEditView(ticket.getId());
        //then
        verify(ticketService1).validateTicketExistence(ticket.getId());
        verify(ticketService1).validateUserPermissionForTicketEditAndDelete(ticket);
        verify(ticketForTicketEditViewMapper).apply(ticket);
        verify(appUserDtoMapper, times(2)).apply(developer);
        assertEquals(ticket.getOptionalDeveloper(), Optional.of(developer));
        assertEquals(1, ticketEditViewDto.getPossibleDevelopers().size());
    }
    @Test
    void createNewTicketTest() {
        //given
        Project project = new Project();
        AppUser appUser = new AppUser();
        appUser.setId(5312L);
        when(appUserService.getUserFromContext()).thenReturn(Optional.of(appUser));
        when(appUserRepository.findById(appUser.getId())).thenReturn(Optional.of(appUser));
        doReturn(null).when(ticketRepository).save(any());
        //when
        Ticket ticket = ticketService.createNewTicket(project);
        //then
        verify(ticketRepository).save(ticket);
        assertEquals(project, ticket.getProject());
        assertNotNull(ticket.getCreatedAt());
        assertEquals(appUser, ticket.getSubmitter());
    }
}