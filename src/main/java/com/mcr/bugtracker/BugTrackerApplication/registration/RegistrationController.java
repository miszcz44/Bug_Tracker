package com.mcr.bugtracker.BugTrackerApplication.registration;

import ch.qos.logback.core.util.Duration;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ApiRequestException;
import com.mcr.bugtracker.BugTrackerApplication.Exceptions.ErrorResponse;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.security.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Basic;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private final AppUserService appUserService;
    private final JwtUtil jwtUtil;
    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        registrationService.register(request);
        return ResponseEntity.ok(null);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        try {
            AppUser user = appUserService.getUserFromContext().orElseThrow();
            Boolean isTokenValid = jwtUtil.isTokenValid(token, user);
            return ResponseEntity.ok(isTokenValid);
        }
        catch (ExpiredJwtException e) {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody AuthCredentialsRequest request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(), request.getPassword()
                            )
                    );

            AppUser user = (AppUser) authenticate.getPrincipal();
            user.setPassword(null);
            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtUtil.generateToken(user)
                    )
                    .body(user);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        //return ResponseEntity.ok(null);
    }
}
