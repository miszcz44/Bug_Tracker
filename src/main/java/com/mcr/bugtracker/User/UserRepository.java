package com.mcr.bugtracker.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<AppUser, Long> {
    List<AppUser> APPLICATION_USERS = Arrays.asList(
            new AppUser("123", "3232", "123", "123@gmail.com", UserRole.ADMIN, false, true)
    );
    Optional<AppUser> findByUsername(String username);
}

