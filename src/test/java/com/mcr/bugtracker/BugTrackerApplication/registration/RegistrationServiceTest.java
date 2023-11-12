package com.mcr.bugtracker.BugTrackerApplication.registration;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.email.EmailSender;
import com.mcr.bugtracker.BugTrackerApplication.registration.token.ConfirmationToken;
import com.mcr.bugtracker.BugTrackerApplication.registration.token.ConfirmationTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class RegistrationServiceTest {
    @Mock
    AppUserService appUserService;
    @Mock
    EmailValidator emailValidator;
    @Mock
    ConfirmationTokenService confirmationTokenService;
    @Mock
    EmailSender emailSender;
    RegistrationService registrationService;
    AutoCloseable autoCloseable;
    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        registrationService = new RegistrationService(appUserService, emailValidator, confirmationTokenService, emailSender);
    }
    @AfterEach
    void tearUp() throws Exception {
        autoCloseable.close();
    }
    @Test
    void registerTest() {
        //given
        RegistrationRequest registrationRequest = new RegistrationRequest(
                "firstName", "lastName", "role", "email", "password");
        RegistrationService registrationService1 = spy(registrationService);
        doNothing().when(registrationService1).validateEmail(registrationRequest.getEmail());
        AppUser appUser = new AppUser();
        when(registrationService1.createUserWithRequest(registrationRequest)).thenReturn(appUser);
        doNothing().when(appUserService).signUpUser(appUser);
        when(appUserService.generateAndSaveConfirmationTokenForGivenUser(appUser)).thenReturn("foo");
        doNothing().when(emailSender).send(any(), any());
        doReturn("foo").when(registrationService1).buildEmail(any(), any());
        //when
        registrationService1.register(registrationRequest);
        //then
        verify(registrationService1).createUserWithRequest(registrationRequest);
        verify(appUserService).signUpUser(appUser);
        verify(appUserService).generateAndSaveConfirmationTokenForGivenUser(appUser);
        verify(emailSender).send(any(), any());
        verify(registrationService1).buildEmail(any(), any());
    }
    @Test
    void createUserWithRequestTest() {
        //given
        RegistrationRequest request = new RegistrationRequest(
                "foo", "foo","Developer","foo","foo");
        //when
        AppUser appUser = registrationService.createUserWithRequest(request);
        //then
        assertEquals(request.getFirstName(), appUser.getFirstName());
        assertEquals(request.getLastName(), appUser.getLastName());
        assertEquals(request.getRole(), appUser.getSRole());
        assertEquals(request.getEmail(), appUser.getEmail());
        assertEquals(request.getPassword(), appUser.getPassword());
    }
    @Test
    void confirmTokenTest() {
        //given
        ConfirmationToken token = new ConfirmationToken(
                "123", LocalDateTime.now(), LocalDateTime.now().plusMinutes(100L), new AppUser());
        when(confirmationTokenService.getToken("123")).thenReturn(Optional.of(token));
        doNothing().when(confirmationTokenService).setConfirmedAt("123");
        doNothing().when(appUserService).enableAppUser(any());
        //when
        registrationService.confirmToken("123");
        //then
        verify(confirmationTokenService).getToken("123");
        verify(confirmationTokenService).setConfirmedAt("123");
        verify(appUserService).enableAppUser(any());
        assertDoesNotThrow(() -> registrationService.confirmToken("123"));
    }
    @Test
    void confirmTokenTest_ShouldThrowEmailAlreadyConfirmed() {
        //given
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        when(confirmationTokenService.getToken("123")).thenReturn(Optional.of(confirmationToken));
        //when
        //then
        assertEquals("Email already confirmed",
                assertThrows(IllegalStateException.class, () -> registrationService.confirmToken("123")).getMessage());
        verify(confirmationTokenService).getToken("123");
    }
    @Test
    void confirmTokenTest_ShouldThrowTokenExpired() {
        //given
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setExpiresAt(LocalDateTime.now().minusMinutes(100L));
        when(confirmationTokenService.getToken("123")).thenReturn(Optional.of(confirmationToken));
        //when
        //then
        assertEquals("Token expired",
                assertThrows(IllegalStateException.class, () -> registrationService.confirmToken("123")).getMessage());
        verify(confirmationTokenService).getToken("123");
    }
}
