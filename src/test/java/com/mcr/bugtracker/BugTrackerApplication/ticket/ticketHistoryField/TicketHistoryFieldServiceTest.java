package com.mcr.bugtracker.BugTrackerApplication.ticket.ticketHistoryField;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserDto;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.ticket.Ticket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TicketHistoryFieldServiceTest {
    @Mock
    TicketHistoryFieldRepository ticketHistoryFieldRepository;
    @Mock
    AppUserService appUserService;
    TicketHistoryFieldService ticketHistoryFieldService;
    AutoCloseable autoCloseable;
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        ticketHistoryFieldService = new TicketHistoryFieldService(ticketHistoryFieldRepository, appUserService);
    }
    @AfterEach
    void tearUp() throws Exception {
        autoCloseable.close();
    }
    @Test
    public void saveChangeOfTitleTest() {
        //given
        TicketHistoryFieldService ticketHistoryFieldService1 = spy(ticketHistoryFieldService);
        Ticket ticket = new Ticket();
        ticket.setTitle("title");
        doNothing().when(ticketHistoryFieldService1).save(any());
        //when
        ticketHistoryFieldService1.saveChangeOfTitle(ticket, "anotherTitle");
        //then
        verify(ticketHistoryFieldService1).save(any());
        assertEquals("anotherTitle", ticket.getTitle());
    }
    @Test
    public void saveChangeOfTitleTest_TitleNull() {
        //given
        Ticket ticket = new Ticket();
        //when
        ticketHistoryFieldService.saveChangeOfTitle(ticket, "anotherTitle");
        //then
        assertEquals("anotherTitle", ticket.getTitle());
    }
    @Test
    public void saveChangeOfTitleTest_TitlesSame() {
        //given
        TicketHistoryFieldService ticketHistoryFieldService1 = spy(ticketHistoryFieldService);
        Ticket ticket = new Ticket();
        ticket.setTitle("anotherTitle");
        //when
        ticketHistoryFieldService1.saveChangeOfTitle(ticket, "anotherTitle");
        //then
        verifyNoInteractions(ticketHistoryFieldRepository);
        assertEquals("anotherTitle", ticket.getTitle());
    }
    @Test
    public void saveChangeOfDescriptionTest() {
        //given
        TicketHistoryFieldService ticketHistoryFieldService1 = spy(ticketHistoryFieldService);
        Ticket ticket = new Ticket();
        ticket.setDescription("description");
        doNothing().when(ticketHistoryFieldService1).save(any());
        //when
        ticketHistoryFieldService1.saveChangeOfDescription(ticket, "anotherDescription");
        //then
        verify(ticketHistoryFieldService1).save(any());
        assertEquals("anotherDescription", ticket.getDescription());
    }
    @Test
    public void saveChangeOfDescriptionTest_DescriptionNull() {
        //given
        Ticket ticket = new Ticket();
        //when
        ticketHistoryFieldService.saveChangeOfDescription(ticket, "anotherDescription");
        //then
        assertEquals("anotherDescription", ticket.getDescription());
    }
    @Test
    public void saveChangeOfDescriptionTest_DescriptionsSame() {
        //given
        TicketHistoryFieldService ticketHistoryFieldService1 = spy(ticketHistoryFieldService);
        Ticket ticket = new Ticket();
        ticket.setDescription("anotherDescription");
        //when
        ticketHistoryFieldService1.saveChangeOfDescription(ticket, "anotherDescription");
        //then
        verifyNoInteractions(ticketHistoryFieldRepository);
        assertEquals("anotherDescription", ticket.getDescription());
    }
    @Test
    public void saveChangeOfDeveloperTest() {
        //given
        TicketHistoryFieldService ticketHistoryFieldService1 = spy(ticketHistoryFieldService);
        Ticket ticket = new Ticket();
        AppUser appUser = new AppUser();
        appUser.setId(135L);
        ticket.setAssignedDeveloper(appUser);
        AppUser appUser1 = new AppUser();
        appUser1.setId(222L);
        AppUserDto appUserDto = new AppUserDto();
        appUserDto.setId(222L);
        doNothing().when(ticketHistoryFieldService1).save(any());
        when(appUserService.findById(appUserDto.getId())).thenReturn(appUser1);
        //when
        ticketHistoryFieldService1.saveChangeOfDeveloper(ticket, appUserDto);
        //then
        verify(ticketHistoryFieldService1).save(any());
        assertEquals(appUser1, ticket.getAssignedDeveloper());
    }
    @Test
    public void saveChangeOfDeveloperTest_SameDevs() {
        //given
        Ticket ticket = new Ticket();
        AppUser appUser = new AppUser();
        appUser.setId(135L);
        ticket.setAssignedDeveloper(appUser);
        AppUserDto appUserDto = new AppUserDto();
        appUserDto.setId(135L);
        //when
        ticketHistoryFieldService.saveChangeOfDeveloper(ticket, appUserDto);
        //then
        verifyNoInteractions(appUserService);
        verifyNoInteractions(ticketHistoryFieldRepository);
        assertEquals(appUser, ticket.getAssignedDeveloper());
    }
    @Test
    public void saveChangeOfDeveloperTest_AssignedDevNull() {
        //given
        Ticket ticket = new Ticket();
        AppUserDto appUserDto = new AppUserDto();
        appUserDto.setId(135L);
        AppUser appUser = new AppUser();
        appUser.setId(135L);
        when(appUserService.findById(appUserDto.getId())).thenReturn(appUser);
        //when
        ticketHistoryFieldService.saveChangeOfDeveloper(ticket, appUserDto);
        //then
        assertEquals(appUser, ticket.getAssignedDeveloper());
    }
    @Test
    public void saveChangeOfTypeTest() {
        //given
        TicketHistoryFieldService ticketHistoryFieldService1 = spy(ticketHistoryFieldService);
        Ticket ticket = new Ticket();
        ticket.setType("type");
        doNothing().when(ticketHistoryFieldService1).save(any());
        //when
        ticketHistoryFieldService1.saveChangeOfType(ticket, "anotherType");
        //then
        verify(ticketHistoryFieldService1).save(any());
        assertEquals("anotherType", ticket.getType());
    }
    @Test
    public void saveChangeOfTypeTest_TypeNull() {
        //given
        Ticket ticket = new Ticket();
        //when
        ticketHistoryFieldService.saveChangeOfType(ticket, "anotherType");
        //then
        assertEquals("anotherType", ticket.getType());
    }
    @Test
    public void saveChangeOfTypeTest_TypesSame() {
        //given
        TicketHistoryFieldService ticketHistoryFieldService1 = spy(ticketHistoryFieldService);
        Ticket ticket = new Ticket();
        ticket.setType("anotherType");
        //when
        ticketHistoryFieldService1.saveChangeOfType(ticket, "anotherType");
        //then
        verifyNoInteractions(ticketHistoryFieldRepository);
        assertEquals("anotherType", ticket.getType());
    }
    @Test
    public void saveChangeOfPriorityTest() {
        //given
        TicketHistoryFieldService ticketHistoryFieldService1 = spy(ticketHistoryFieldService);
        Ticket ticket = new Ticket();
        ticket.setPriority("priority");
        doNothing().when(ticketHistoryFieldService1).save(any());
        //when
        ticketHistoryFieldService1.saveChangeOfPriority(ticket, "anotherPriority");
        //then
        verify(ticketHistoryFieldService1).save(any());
        assertEquals("anotherPriority", ticket.getPriority());
    }
    @Test
    public void saveChangeOfPriorityTest_PriorityNull() {
        //given
        Ticket ticket = new Ticket();
        //when
        ticketHistoryFieldService.saveChangeOfPriority(ticket, "anotherPriority");
        //then
        assertEquals("anotherPriority", ticket.getPriority());
    }
    @Test
    public void saveChangeOfPriorityTest_PrioritiesSame() {
        //given
        TicketHistoryFieldService ticketHistoryFieldService1 = spy(ticketHistoryFieldService);
        Ticket ticket = new Ticket();
        ticket.setPriority("anotherPriority");
        //when
        ticketHistoryFieldService1.saveChangeOfPriority(ticket, "anotherPriority");
        //then
        verifyNoInteractions(ticketHistoryFieldRepository);
        assertEquals("anotherPriority", ticket.getPriority());
    }

    @Test
    public void saveChangeOfStatusTest() {
        //given
        TicketHistoryFieldService ticketHistoryFieldService1 = spy(ticketHistoryFieldService);
        Ticket ticket = new Ticket();
        ticket.setStatus("status");
        doNothing().when(ticketHistoryFieldService1).save(any());
        //when
        ticketHistoryFieldService1.saveChangeOfStatus(ticket, "anotherStatus");
        //then
        verify(ticketHistoryFieldService1).save(any());
        assertEquals("anotherStatus", ticket.getStatus());
    }
    @Test
    public void saveChangeOfStatusTest_StatusNull() {
        //given
        Ticket ticket = new Ticket();
        //when
        ticketHistoryFieldService.saveChangeOfStatus(ticket, "anotherStatus");
        //then
        assertEquals("anotherStatus", ticket.getStatus());
    }
    @Test
    public void saveChangeOfStatusTest_PrioritiesSame() {
        //given
        TicketHistoryFieldService ticketHistoryFieldService1 = spy(ticketHistoryFieldService);
        Ticket ticket = new Ticket();
        ticket.setStatus("anotherStatus");
        //when
        ticketHistoryFieldService1.saveChangeOfStatus(ticket, "anotherStatus");
        //then
        verifyNoInteractions(ticketHistoryFieldRepository);
        assertEquals("anotherStatus", ticket.getStatus());
    }
}