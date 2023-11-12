package com.mcr.bugtracker.BugTrackerApplication.appuser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
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