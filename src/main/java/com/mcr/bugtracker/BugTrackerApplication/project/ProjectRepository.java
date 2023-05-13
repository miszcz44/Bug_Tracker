package com.mcr.bugtracker.BugTrackerApplication.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(value = "SELECT project_id FROM project_personnel WHERE user_id = ?1",
            nativeQuery = true)
    List<Long> findAllProjectsIdsAssignedToUser(Long id);

    @Query(value = "SELECT * FROM project WHERE id IN ?1",
            nativeQuery = true)
    List<Project> findByIds(List<Long> projectIds);

    @Query(value = "DELETE FROM project_personnel WHERE user_id = ?1",
            nativeQuery = true)
    void deleteUserFromProject(Long userId);
}
