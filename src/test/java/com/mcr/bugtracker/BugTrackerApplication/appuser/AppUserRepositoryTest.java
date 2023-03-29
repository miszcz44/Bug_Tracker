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
    @Test
    void findByEmail() {
        // create test user
        AppUser user = new AppUser("Mikołaj", "Bultrowicz", "bultron@gmail.com", "12345", AppUserRole.USER);

        // insert test user into database
        underTest.save(user);

        // retrieve user by email
        Optional<AppUser> expectedUser = underTest.findByEmail("bultron@gmail.com");

        // assert that user is found
        assertTrue(expectedUser.isPresent());

        // assert that user is correct
        assertEquals(user, expectedUser.get());

        // retrieve non-existing user by email
        Optional<AppUser> nonExistingUser = underTest.findByEmail("nonexistinguser@gmail.com");

        // assert that non-existing user is not found
        assertFalse(nonExistingUser.isPresent());

    }
}