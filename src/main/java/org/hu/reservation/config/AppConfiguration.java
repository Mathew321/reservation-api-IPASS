package org.hu.reservation.config;

import org.hu.reservation.service.StaffInfoService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

@org.springframework.context.annotation.Configuration
public class AppConfiguration {

    @Bean
    public StaffInfoService staffInfoService() {
        return new StaffInfoService();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        StaffInfoService staffInfoService = staffInfoService();
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(staffInfoService);
        authenticationProvider.setPasswordEncoder(staffInfoService.getEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
