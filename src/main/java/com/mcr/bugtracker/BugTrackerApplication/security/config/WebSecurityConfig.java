package com.mcr.bugtracker.BugTrackerApplication.security.config;

import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUser;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserRole;
import com.mcr.bugtracker.BugTrackerApplication.appuser.AppUserService;
import com.mcr.bugtracker.BugTrackerApplication.registration.RegistrationService;
import com.mcr.bugtracker.BugTrackerApplication.security.JwtFilter;
import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtFilter jwtFilter;

    @Override @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().cors().disable()
                .authorizeRequests()
                    .antMatchers("/api/v1/user/dashboard").hasAnyAuthority(
                            "PROJECT_MANAGER", "ADMIN", "DEVELOPER", "SUBMITTER", "NONE")
                    .antMatchers("/api/v1/user/role-management/**").hasAuthority("ADMIN")
                    .antMatchers(HttpMethod.POST, "/api/v1/project").hasAnyAuthority(
                            "ADMIN", "PROJECT_MANAGER")
                    .antMatchers(HttpMethod.DELETE, "/api/v1/project").hasAnyAuthority(
                        "ADMIN", "PROJECT_MANAGER")
                    .antMatchers("/api/v1/project/edit/**").hasAnyAuthority("ADMIN", "PROJECT_MANAGER")
                    .antMatchers(HttpMethod.POST, "/api/v1/ticket").hasAnyAuthority(
                        "ADMIN", "PROJECT_MANAGER", "SUBMITTER")
                    .antMatchers(HttpMethod.DELETE, "/api/v1/ticket").hasAnyAuthority(
                        "ADMIN", "PROJECT_MANAGER")
                    .antMatchers("/api/v1/ticket/details/**").hasAnyAuthority(
                            "ADMIN", "PROJECT_MANAGER", "DEVELOPER", "SUBMITTER")
                    .antMatchers("/api/v1/ticket/edit/**").hasAnyAuthority(
                            "ADMIN", "PROJECT_MANAGER", "SUBMITTER")
//                    .antMatchers("/api/v1/userx").hasAuthority("USER")
                    .antMatchers("/api/v1/registration/login").permitAll()
                    .antMatchers("/api/v1/registration").permitAll()
                    .antMatchers("/api/v1/registration/confirm").permitAll()
                    .antMatchers("/api/v1/registration/validate").permitAll()
//                    //.antMatchers("/api/v1/ticket").permitAll()
//                    //.antMatchers("/api/v1/ticket/*").permitAll()
//                    .antMatchers("/api/v1/project/*").permitAll()
//                    .antMatchers("/api/v1/project").permitAll()
//                    .antMatchers("/api/v1/**").permitAll()
//                    .antMatchers("/api/v1/user/role/*").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler());;
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//
//        auth
//                .inMemoryAuthentication()
//                .withUser("user").password("{noop}password").roles("USER")
//                .and()
//                .withUser("admin").password("{noop}password").roles("ADMIN")
//                .and()
//                .withUser("newuser").password("{noop}password").roles("USER");
//    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(appUserService);
        return provider;
    }

    private class CustomAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response,
                           AccessDeniedException accessDeniedException) throws IOException, ServletException {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("You do not have permission to access this resource.");
        }
    }
}
