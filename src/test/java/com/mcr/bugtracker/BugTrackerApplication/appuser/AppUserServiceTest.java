package com.mcr.bugtracker.BugTrackerApplication.appuser;

import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiRequestException;
import com.mcr.bugtracker.BugTrackerApplication.registration.token.ConfirmationTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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
}