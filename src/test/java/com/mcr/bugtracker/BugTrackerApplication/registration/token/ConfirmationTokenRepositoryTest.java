package com.mcr.bugtracker.BugTrackerApplication.registration.token;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class ConfirmationTokenRepositoryTest {

    @Autowired
    private ConfirmationTokenRepository underTest;
    @Autowired
    private AppUserRepository userRepository;

    @Test
    void updateConfirmedAt() {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        String token = "123";
        // must set all of those fields cause they are not nullable
        confirmationToken.setToken(token);
        confirmationToken.setCreatedAt(LocalDateTime.now());
        confirmationToken.setExpiresAt(LocalDateTime.now());
        AppUser appUser = new AppUser();
        userRepository.save(appUser);
        confirmationToken.setAppUser(appUser);

        underTest.save(confirmationToken);
        LocalDateTime now = LocalDateTime.now();
        underTest.updateConfirmedAt(token, now);
        Optional<ConfirmationToken> dbTokenOpt = underTest.findByToken(token);
        ConfirmationToken dbToken = dbTokenOpt.get();
        assertEquals(dbToken.getConfirmedAt().truncatedTo(ChronoUnit.MILLIS), now.truncatedTo(ChronoUnit.MILLIS));
    }
}