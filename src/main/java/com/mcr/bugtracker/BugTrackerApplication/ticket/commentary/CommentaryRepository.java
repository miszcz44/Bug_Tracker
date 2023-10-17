package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentaryRepository extends JpaRepository<Commentary, Long> {}
