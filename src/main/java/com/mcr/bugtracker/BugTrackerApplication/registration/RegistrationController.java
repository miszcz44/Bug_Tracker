package com.mcr.bugtracker.BugTrackerApplication.registration;

import ch.qos.logback.core.util.Duration;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;


@RestController
@RequestMapping(path = "api/v1/registration")
@ConfigurationProperties("cookies")
@Slf4j
public class RegistrationController {
    private final RegistrationService registrationService;
    private final AuthenticationManager authenticationManager;
    private final AppUserService appUserService;
    private final JwtUtil jwtUtil;

    public RegistrationController(RegistrationService registrationService, AuthenticationManager authenticationManager, AppUserService appUserService, JwtUtil jwtUtil) {
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
        this.appUserService = appUserService;
        this.jwtUtil = jwtUtil;
    }
    @Value("${cookies.domain}")
    private String domain;
    @PostMapping
    public void register(@RequestBody RegistrationRequest request) {
        registrationService.register(request);
    }
    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
    @GetMapping("/validate")
    public Boolean validateToken(@RequestParam String token) {
        return jwtUtil.isTokenValid(token, appUserService.getUserFromContext().orElseThrow());
    }
    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody AuthCredentialsRequest request) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
                    );
            AppUser user = (AppUser) authenticate.getPrincipal();
            user.setPassword(null);
            String token = jwtUtil.generateToken(user);
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .domain(domain)
                    .path("/")
                    .maxAge(Duration.buildByDays(365).getMilliseconds())
                    .build();
            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(token);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<?> logout () {
        ResponseCookie cookie = ResponseCookie.from("jwt", "")
                .domain(domain)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString()).body(null);
    }
}
