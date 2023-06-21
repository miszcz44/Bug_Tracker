package com.mcr.bugtracker.BugTrackerApplication.appuser;

import com.mcr.bugtracker.BugTrackerApplication.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;



@Transactional(readOnly = true)
@Repository
public interface AppUserRepository
        extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);

    @Query(value = "SELECT * FROM app_user a " +
            "JOIN project_personnel p ON a.id = p.user_id " +
            "WHERE p.project_id = ?1",
            nativeQuery = true)
    List<AppUser> getProjectPersonnel(Long id);

    @Query(value = "SELECT * FROM app_user WHERE id != ?1 AND id NOT IN ?2",
            nativeQuery = true)
    List<AppUser> getAllUsersButProjectManagerAndPersonnel(Long id, List<Long> personnelIds);

    @Query(value = "SELECT * FROM app_user WHERE id != ?1",
            nativeQuery = true)
    List<AppUser> getAllUsersButProjectManager(Long projectManagerId);


    @Query(value = "SELECT * FROM app_user WHERE app_user_role != 'ADMIN'",
            nativeQuery = true)
    List<AppUser> getAllUsersExceptAdmins();

    @Query(value = "SELECT app_user_role FROM app_user WHERE email = ?1",
            nativeQuery = true)
    AppUserRole findRoleByEmail(String email);
}
