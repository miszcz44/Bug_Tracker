package com.mcr.bugtracker.BugTrackerApplication.appuser;

import com.mcr.bugtracker.BugTrackerApplication.email.EmailSender;
import com.mcr.bugtracker.BugTrackerApplication.registration.token.ConfirmationToken;
import com.mcr.bugtracker.BugTrackerApplication.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Getter
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public void checkIfEmailTakenOrNotConfirmed(AppUser appUser) {
        Optional<AppUser> potentialUser = appUserRepository
                .findByEmail(appUser.getEmail());

        if (!potentialUser.equals(Optional.empty())) {
            AppUser existingUser = potentialUser.get();
            if(!existingUser.getEnabled()) {
                throw new IllegalStateException("Confirm email");
            }
            throw new IllegalStateException("email already taken");
        }
    }

    public String generateAndSaveConfirmationTokenForGivenUser(AppUser appUser) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(
                confirmationToken);
        return token;
    }
    public void signUpUser(AppUser appUser) {
        checkIfEmailTakenOrNotConfirmed(appUser);

        String encodedPassword = bCryptPasswordEncoder
                .encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        appUserRepository.save(appUser);
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }

    public void deleteUser(Long userId) {
        if(!appUserRepository.existsById(userId)) {
            throw new IllegalStateException("User of this id does not exist");
        }
        appUserRepository.deleteById(userId);
    }
}
