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

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;
    @Test
    public void enableAppUserTest() {
        //given
        AppUser appUser = new AppUser();
        appUser.setId(523L);
        appUser.setEmail("randomEmail");
        appUserRepository.save(appUser);
        //when
        appUserRepository.enableAppUser("randomEmail");
        AppUser userFromrepo = appUserRepository.findByEmail("randomEmail").orElseThrow();
        //then
        assertTrue(userFromrepo.getEnabled());
    }
}