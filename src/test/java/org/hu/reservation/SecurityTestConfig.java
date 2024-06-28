package org.hu.reservation;

import org.hu.reservation.filter.JwtAuthFilter;
import org.hu.reservation.repository.StaffInfoRepository;
import org.hu.reservation.service.JwtService;
import org.hu.reservation.service.ReservationService;
import org.hu.reservation.service.StaffInfoService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationProvider;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class SecurityTestConfig {

    @Bean
    @Primary
    public JwtAuthFilter jwtAuthFilter() {
        return mock(JwtAuthFilter.class);

    }
    @Bean
    @Primary
    public JwtService jwtService() {
        return mock(JwtService.class);
    }

    @Bean
    @Primary
    public StaffInfoService staffInfoService() {
        return mock(StaffInfoService.class);
    }

    @Bean
    @Primary
    public StaffInfoRepository staffInfoRepository() {
        return mock(StaffInfoRepository.class);
    }

    @Bean
    @Primary
    public AuthenticationProvider authenticationProvider() {
        return mock(AuthenticationProvider.class);
    }

}
