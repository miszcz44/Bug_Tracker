package com.mcr.bugtracker.BugTrackerApplication.appuser;

import com.mcr.bugtracker.BugTrackerApplication.registration.token.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AppUserServiceTest {

    @Mock
    private AppUserRepository repository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ConfirmationTokenService confirmationTokenService;
    private AppUserService underTest;
    private EntityManager entityManager;
    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    @BeforeEach
    void setUp() {
        underTest = new AppUserService(repository, bCryptPasswordEncoder, confirmationTokenService);
    }

    @Test
    public void testLoadUserByUsernameWhenUserNotFound() {
        String email = "non-existent-user@example.com";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining(String.format(USER_NOT_FOUND_MSG, email));
    }

    @Test
    public void testLoadUserByUsernameWhenUserExists() {
        String email = "existing-user@example.com";
        AppUser user = new AppUser("1","2", email, "password", AppUserRole.ADMIN);
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        UserDetails userDetails = underTest.loadUserByUsername(email);
        assertEquals(email, userDetails.getUsername());
    }

    @Test
    public void testCheckIfEmailTakenOrNotConfirmedWhenNotConfirmed() {
        String email = "existing-user@example.com";
        AppUser user = new AppUser("1","2", email, "password", AppUserRole.ADMIN);
        user.setEnabled(false); // it's also false by default
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThatThrownBy(() -> underTest.checkIfEmailTakenOrNotConfirmed(user))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Confirm email");
    }

    @Test
    public void testCheckIfEmailTakenOrNotConfirmedWhenEmailTaken() {
        String email = "existing-user@example.com";
        AppUser user = new AppUser("1","2", email, "password", AppUserRole.ADMIN);
        user.setEnabled(true);
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThatThrownBy(() -> underTest.checkIfEmailTakenOrNotConfirmed(user))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email already taken");
    }

    @Test
    void signUpUser() {
    }

    @Test
    void enableAppUser() {
    }
}