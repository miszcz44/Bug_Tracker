package com.mcr.bugtracker.BugTrackerApplication.ticket.commentary;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
public class Commentary {
    @SequenceGenerator(
            name = "commentary_sequence",
            sequenceName = "commentary_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "commentary_sequence"
    )
    private Long id;
    @ManyToOne
    private AppUser commenter;
    private String message;
    private LocalDateTime createdAt;
}
