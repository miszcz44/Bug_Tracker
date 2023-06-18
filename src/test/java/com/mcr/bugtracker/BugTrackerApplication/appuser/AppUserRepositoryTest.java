package com.mcr.bugtracker.BugTrackerApplication.appuser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository underTest;

    @Mock
    private JdbcTemplate jdbcTemplate;
    // testing functions provided with jpa repository is useless, this will be deleted
    @Test
    void checkIfUserFoundByEmail() {
        AppUser user = new AppUser("Mikołaj", "Bultrowicz", "bultron@gmail.com", "12345", AppUserRole.ADMIN);
        underTest.save(user);
        Optional<AppUser> expectedUser = underTest.findByEmail("bultron@gmail.com");
        assertTrue(expectedUser.isPresent());
        assertEquals(user.getId(), expectedUser.get().getId());
    }
    @Test
    void checkIfNonExistingUserNotFoundByEmail() {
        Optional<AppUser> nonExistingUser = underTest.findByEmail("nonexistinguser@gmail.com");
        assertFalse(nonExistingUser.isPresent());
    }
    @Test
    void testEnableAppUserSuccess() {
        AppUser user = new AppUser();
        String email = "test@example.com";
        user.setEmail(email);
        underTest.save(user);
        underTest.enableAppUser(email);
        Optional<AppUser> enabledUser = underTest.findByEmail(email);
        assertTrue(enabledUser.get().getEnabled());
    }
}