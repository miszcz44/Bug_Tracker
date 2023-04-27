package com.mcr.bugtracker.BugTrackerApplication.ticket;

import com.mcr.bugtracker.BugTrackerApplication.registration.RegistrationRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/ticket")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<?> createEmptyTicket() {
        Ticket ticket = new Ticket();
        ticketService.saveTicket(ticket);
        return ResponseEntity.ok(ticket);
    }

    @GetMapping
    public ResponseEntity<?> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/types")
    public ResponseEntity<?> getTicketTypes() {
        return ResponseEntity.ok(ticketService.getTicketTypes());
    }

    @GetMapping("{ticketId}")
    public ResponseEntity<?> getTicketDataById(@PathVariable Long ticketId) {
        Optional<Ticket> ticketOpt = ticketService.findById(ticketId);
        return ResponseEntity.ok(ticketOpt.orElse(new Ticket()));
    }

    @PutMapping("{ticketId}")
    public ResponseEntity<?> updateTicketData(@RequestBody Ticket ticket) {
        Ticket updatedTicket = ticketService.saveTicket(ticket);
        return ResponseEntity.ok(updatedTicket);
    }

}
