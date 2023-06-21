package com.mcr.bugtracker.BugTrackerApplication.appuser;

import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiRequestException;
import com.mcr.bugtracker.BugTrackerApplication.email.EmailSender;
import com.mcr.bugtracker.BugTrackerApplication.registration.token.ConfirmationToken;
import com.mcr.bugtracker.BugTrackerApplication.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

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
    public AppUser loadUserByUsername(String email)
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
                throw new ApiRequestException("Confirm email");
            }
            throw new ApiRequestException("email already taken");
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

    public Optional<AppUser> getUserFromContext() {
        AppUser user = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return appUserRepository.findById(user.getId());
    }

    public String getNameOfTheLoggedUser() {
        AppUser user = getUserFromContext().orElseThrow();
        return user.getFirstName() + " " + user.getLastName();
    }

    public List<AppUser> getAllUsersExceptTheLoggedOneAndProjectPersonnel(List<AppUser> projectPersonnel) {
        List<Long> idList = projectPersonnel.stream()
                .map(AppUser::getId)
                .collect(Collectors.toList());
        Long projectManagerId = getUserFromContext().orElseThrow().getId();
        if(idList.isEmpty()) {
            return appUserRepository.getAllUsersButProjectManager(projectManagerId);
        }
        return appUserRepository.getAllUsersButProjectManagerAndPersonnel(projectManagerId, idList);
    }


    public List<AppUser> findAllUsersAssignedToProject(Long id) {
        return appUserRepository.getProjectPersonnel(id);
    }

    public List<AppUser> getAllUsersExceptAdmins() {
        return appUserRepository.getAllUsersExceptAdmins();
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public void changeUsersRole(List<String> usersEmails, AppUserRole assignedRole) {
        for(String email : usersEmails) {
            AppUser user = appUserRepository.findByEmail(email).get();
            user.setAppUserRole(assignedRole);
            appUserRepository.save(user);
        }
    }

    public String getRoleByEmail(String email) {
        return appUserRepository.findRoleByEmail(email).getName();
    }
}
