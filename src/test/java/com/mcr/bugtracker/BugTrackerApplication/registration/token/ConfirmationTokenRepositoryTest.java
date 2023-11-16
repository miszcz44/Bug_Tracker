package com.mcr.bugtracker.BugTrackerApplication.registration.token;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ConfirmationTokenRepositoryTest {

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    AppUserRepository appUserRepository;
    @Test
    public void updateConfirmedAtTest() {
        //given
        AppUser appUser = new AppUser();
        appUserRepository.save(appUser);
        ConfirmationToken confirmationToken = new ConfirmationToken(
                "123", LocalDateTime.now(), LocalDateTime.now(), appUser);
        confirmationTokenRepository.save(confirmationToken);
        //when
        confirmationTokenRepository.updateConfirmedAt("123", LocalDateTime.of(2010, 5, 3, 2, 2));
        ConfirmationToken tokenFromRepo = confirmationTokenRepository.findById(confirmationToken.getId()).orElseThrow();
        //then
        assertEquals(LocalDateTime.of(2010, 5, 3, 2, 2), tokenFromRepo.getConfirmedAt());
    }
}