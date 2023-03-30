package com.mcr.bugtracker.BugTrackerApplication.appuser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository underTest;
    // testing functions provided with jpa repository is useless, this will be deleted
    @Test
    void checkIfUserFoundByEmail() {
        AppUser user = new AppUser("Mikołaj", "Bultrowicz", "bultron@gmail.com", "12345", AppUserRole.USER);
        underTest.save(user);
        Optional<AppUser> expectedUser = underTest.findByEmail("bultron@gmail.com");
        assertTrue(expectedUser.isPresent());
        assertEquals(user, expectedUser.get());
    }
    @Test
    void checkIfNonExistingUserNotFoundByEmail() {
        Optional<AppUser> nonExistingUser = underTest.findByEmail("nonexistinguser@gmail.com");
        assertFalse(nonExistingUser.isPresent());
    }
}