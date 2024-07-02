package org.hu.reservation.service;

import org.hu.reservation.entity.StaffInfo;
import org.hu.reservation.repository.StaffInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StaffInfoServiceTest {

    @Mock
    private StaffInfoRepository repository;

    @InjectMocks
    private StaffInfoService service;

    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        encoder = service.getEncoder();
    }

    @Test
    void loadUserByUsername_UserExists() {
        StaffInfo staffInfo = new StaffInfo(1, "john", "john.doe@example.com", "password", "ROLE_USER");
        when(repository.findByName("john")).thenReturn(Optional.of(staffInfo));

        UserDetails userDetails = service.loadUserByUsername("john");

        assertNotNull(userDetails);
        assertEquals("john", userDetails.getUsername());
        verify(repository, times(1)).findByName("john");
    }

    @Test
    void loadUserByUsername_UserDoesNotExist() {
        when(repository.findByName("john")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("john"));

        verify(repository, times(1)).findByName("john");
    }

    @Test
    void addStaffMember_Success() {
        StaffInfo staffInfo = new StaffInfo(0, "john", "john.doe@example.com", "password", "ROLE_USER");
        StaffInfo savedStaffInfo = new StaffInfo(1, "john", "john.doe@example.com", encoder.encode("password"), "ROLE_USER");

        when(repository.save(any(StaffInfo.class))).thenReturn(savedStaffInfo);

        int id = service.addStaffMemeber(staffInfo);

        assertEquals(1, id);
        assertTrue(encoder.matches("password", savedStaffInfo.getPassword()));
        verify(repository, times(1)).save(any(StaffInfo.class));
    }
}
